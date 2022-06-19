package com.spring.cms.repository.member;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.cms.domain.Member;
import com.spring.cms.domain.QAuthority;
import com.spring.cms.domain.QMemberAuthority;
import com.spring.cms.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.spring.cms.domain.QAuthority.authority1;
import static com.spring.cms.domain.QMember.member;
import static com.spring.cms.domain.QMemberAuthority.memberAuthority;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MemberDto.MemberResponse> searchMembers(Pageable pageable, String search) {
        List<MemberDto.MemberResponse> members = searchMemberToMemberDtoMemberResponse(pageable, search);

        Map<Long, List<MemberAuthorityDto.Response>> memberAuthorityMap = findMemberAuthorityMap(toMemberIds(members));

        members.forEach(m -> m.setAuthorities(memberAuthorityMap.get(m.getId())));

        return members;
    }

    private List<MemberDto.MemberResponse> searchMemberToMemberDtoMemberResponse(Pageable pageable, String search) {
        return queryFactory
                .select(new QMemberDto_MemberResponse(
                        member.id,
                        member.name,
                        member.email,
                        member.hp,
                        member.status,
                        member.createBy,
                        member.lastModifiedBy,
                        member.createdDate,
                        member.lastModifiedDate
                ))
                .from(member)
                .where(nameOrEmailOrHpContains(search))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(member.createdDate.desc(), member.id.desc())
                .fetch();
    }

    private Map<Long, List<MemberAuthorityDto.Response>> findMemberAuthorityMap(List<Long> memberIds) {
        List<MemberAuthorityDto.Response> memberAuthorities = queryFactory
                .select(new QMemberAuthorityDto_Response(
                        memberAuthority.member.id,
                        authority1.id,
                        authority1.authority,
                        authority1.authorityName,
                        authority1.authorityType
                ))
                .from(memberAuthority)
                .leftJoin(memberAuthority.authority, authority1)
                .where(memberAuthority.member.id.in(memberIds))
                .fetch();

        return memberAuthorities.stream()
                .collect(Collectors.groupingBy(ma -> ma.getMemberId()));
    }

    private List<Long> toMemberIds(List<MemberDto.MemberResponse> members) {
        return members.stream()
                .map(m -> m.getId())
                .collect(Collectors.toList());
    }

    @Override
    public Integer searchMembersTotalCount(Pageable pageable, String search) {
        return queryFactory
                .selectFrom(member)
                .where(nameOrEmailOrHpContains(search))
                .fetch().size();
    }

    private BooleanExpression nameOrEmailOrHpContains(String search) {
        return StringUtils.hasText(search) ?
                member.name.contains(search)
                        .or(member.email.contains(search))
                        .or(member.hp.contains(search))
                : null;
    }
}
