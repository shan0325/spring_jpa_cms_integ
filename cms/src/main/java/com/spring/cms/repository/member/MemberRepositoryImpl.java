package com.spring.cms.repository.member;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.cms.domain.Member;
import com.spring.cms.domain.QMember;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;


import org.springframework.data.domain.Pageable;

import static com.spring.cms.domain.QMember.member;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Member> searchMembers(Pageable pageable, String search) {
        QueryResults<Member> memberQueryResults = queryFactory
                .selectFrom(member)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return null;
    }
}
