package com.spring.cms.service;

import com.spring.cms.dto.AuthorityDto;
import com.spring.cms.enums.AuthorityType;

import java.util.List;

public interface AuthorityService {
    List<AuthorityDto.Response> getAuthorities(AuthorityType authorityType);
}
