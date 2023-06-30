package com.spring.cms.service;

import com.spring.cms.dto.MemberDto;
import com.spring.cms.repository.member.dto.MemberQueryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberService {
    MemberDto.Response createMember(MemberDto.Create createMember);
    Page<MemberQueryDto.SearchMembersResponse> searchMembers(Pageable pageable, String search);
    MemberDto.Response getMember(Long memberId);
    void updateMember(MemberDto.Update updateMember);
}
