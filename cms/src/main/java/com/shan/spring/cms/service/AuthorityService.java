package com.shan.spring.cms.service;

import com.shan.spring.cms.dto.AuthorityDto;
import com.shan.spring.cms.enums.AuthorityTypeEnum;

import java.util.List;

public interface AuthorityService {
    List<AuthorityDto.Response> getAuthorities(AuthorityTypeEnum authorityType);
}
