package com.shan.spring.cms.repository.member;

import com.shan.spring.cms.repository.member.dto.MemberQueryDto;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface MemberRepositoryCustom {

    List<MemberQueryDto.SearchMembersResponse> searchMembers(Pageable pageable, String search);

    Integer searchMembersTotalCount(Pageable pageable, String search);
}
