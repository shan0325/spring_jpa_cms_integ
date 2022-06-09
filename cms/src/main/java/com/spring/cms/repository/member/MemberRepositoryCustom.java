package com.spring.cms.repository.member;

import com.spring.cms.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface MemberRepositoryCustom {

    Page<Member> searchMembers(Pageable pageable, String search);
}
