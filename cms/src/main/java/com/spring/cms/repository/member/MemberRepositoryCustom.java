package com.spring.cms.repository.member;

import com.spring.cms.dto.member.MemberQueryDto;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface MemberRepositoryCustom {

    List<MemberQueryDto.SearchMembersResponse> searchMembers(Pageable pageable, String search);

    Integer searchMembersTotalCount(Pageable pageable, String search);
}
