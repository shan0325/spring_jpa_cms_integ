package com.spring.cms.service;

import com.spring.cms.dto.AuthorityDto;
import com.spring.cms.enums.AuthorityTypeEnum;

import java.util.List;

public interface AuthorityService {
    List<AuthorityDto.Response> getAuthorities(AuthorityTypeEnum authorityType);
}
