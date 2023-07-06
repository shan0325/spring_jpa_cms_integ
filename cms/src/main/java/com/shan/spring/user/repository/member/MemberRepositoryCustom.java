package com.shan.spring.user.repository.member;

import com.shan.spring.user.repository.member.dto.MemberQueryDto;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface MemberRepositoryCustom {

    List<MemberQueryDto.SearchMembersResponse> searchMembers(Pageable pageable, String search);

    Integer searchMembersTotalCount(Pageable pageable, String search);
}
