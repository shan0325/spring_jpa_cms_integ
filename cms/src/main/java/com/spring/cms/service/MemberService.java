package com.spring.cms.service;

import com.spring.cms.dto.MemberDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberService {
    MemberDto.Response createMember(MemberDto.Create createMember);
    Page<MemberDto.MemberResponse> searchMembers(Pageable pageable, String search);
    MemberDto.Response getMember(Long memberId);
    void updateMember(MemberDto.Update updateMember);
}
