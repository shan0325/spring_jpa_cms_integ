package com.shan.spring.cms.repository.member;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shan.spring.cms.domain.QAuthority;
import com.shan.spring.cms.domain.QMember;
import com.shan.spring.cms.domain.QMemberAuthority;
import com.shan.spring.cms.repository.member.dto.MemberAuthorityQueryDto;
import com.shan.spring.cms.repository.member.dto.MemberQueryDto;
import com.shan.spring.cms.repository.member.dto.QMemberAuthorityQueryDto_Response;
import com.shan.spring.cms.repository.member.dto.QMemberQueryDto_SearchMembersResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MemberQueryDto.SearchMembersResponse> searchMembers(Pageable pageable, String search) {
        List<MemberQueryDto.SearchMembersResponse> members = searchMemberToMemberDtoMemberResponse(pageable, search);

        Map<Long, List<MemberAuthorityQueryDto.Response>> memberAuthorityMap = findMemberAuthorityMap(toMemberIds(members));

        members.forEach(m -> m.setAuthorities(memberAuthorityMap.get(m.getId())));

        return members;
    }

    private List<MemberQueryDto.SearchMembersResponse> searchMemberToMemberDtoMemberResponse(Pageable pageable, String search) {
        return queryFactory
                .select(new QMemberQueryDto_SearchMembersResponse(
                        QMember.member.id,
                        QMember.member.name,
                        QMember.member.email,
                        QMember.member.hp,
                        QMember.member.status,
                        QMember.member.createBy,
                        QMember.member.lastModifiedBy,
                        QMember.member.createdDate,
                        QMember.member.lastModifiedDate
                ))
                .from(QMember.member)
                .where(nameOrEmailOrHpContains(search))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(QMember.member.createdDate.desc(), QMember.member.id.desc())
                .fetch();
    }

    private Map<Long, List<MemberAuthorityQueryDto.Response>> findMemberAuthorityMap(List<Long> memberIds) {
        List<MemberAuthorityQueryDto.Response> memberAuthorities = queryFactory
                .select(new QMemberAuthorityQueryDto_Response(
                        QMemberAuthority.memberAuthority.member.id,
                        QAuthority.authority1.id,
                        QAuthority.authority1.authority,
                        QAuthority.authority1.authorityName,
                        QAuthority.authority1.authorityType
                ))
                .from(QMemberAuthority.memberAuthority)
                .leftJoin(QMemberAuthority.memberAuthority.authority, QAuthority.authority1)
                .where(QMemberAuthority.memberAuthority.member.id.in(memberIds))
                .fetch();

        return memberAuthorities.stream()
                .collect(Collectors.groupingBy(ma -> ma.getMemberId()));
    }

    private List<Long> toMemberIds(List<MemberQueryDto.SearchMembersResponse> members) {
        return members.stream()
                .map(m -> m.getId())
                .collect(Collectors.toList());
    }

    @Override
    public Integer searchMembersTotalCount(Pageable pageable, String search) {
        return queryFactory
                .selectFrom(QMember.member)
                .where(nameOrEmailOrHpContains(search))
                .fetch().size();
    }

    private BooleanExpression nameOrEmailOrHpContains(String search) {
        return StringUtils.hasText(search) ?
                QMember.member.name.contains(search)
                        .or(QMember.member.email.contains(search))
                        .or(QMember.member.hp.contains(search))
                : null;
    }
}
