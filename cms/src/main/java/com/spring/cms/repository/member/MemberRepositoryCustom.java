package com.spring.cms.repository.member;

import com.spring.cms.domain.Member;
import com.spring.cms.dto.MemberDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface MemberRepositoryCustom {

    List<MemberDto.MemberResponse> searchMembers(Pageable pageable, String search);

    Integer searchMembersTotalCount(Pageable pageable, String search);
}
