package com.spring.cms.service.impl;

import com.spring.cms.domain.Authority;
import com.spring.cms.domain.Manager;
import com.spring.cms.domain.ManagerAuthority;
import com.spring.cms.dto.AuthorityDto;
import com.spring.cms.enums.AuthorityType;
import com.spring.cms.exception.AuthorityException;
import com.spring.cms.exception.MemberException;
import com.spring.cms.repository.AuthorityRepository;
import com.spring.cms.repository.ManagerRepository;
import com.spring.cms.service.AuthorityService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.spring.cms.exception.AuthorityException.AuthorityExceptionType.NOT_EXIST_AUTHORITY;
import static com.spring.cms.exception.MemberException.MemberExceptionType.NOT_FOUND_MEMBER;

@RequiredArgsConstructor
@Service
public class AuthorityServiceImpl implements AuthorityService {

    private final AuthorityRepository authorityRepository;

    private final ModelMapper modelMapper;

    @Override
    public List<AuthorityDto.Response> getAuthorities(AuthorityType authorityType) {
        return authorityRepository.findByAuthorityType(authorityType)
                .orElseThrow(() -> new AuthorityException(NOT_EXIST_AUTHORITY))
                .stream()
                .map(a -> modelMapper.map(a, AuthorityDto.Response.class))
                .collect(Collectors.toList());
    }
}
