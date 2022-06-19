package com.spring.cms.service;

import com.spring.cms.domain.Authority;
import com.spring.cms.dto.AuthorityDto;
import com.spring.cms.enums.AuthorityType;
import com.spring.cms.exception.AuthorityException;
import com.spring.cms.repository.AuthorityRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.spring.cms.exception.AuthorityException.AuthorityExceptionType.NOT_EXIST_AUTHORITY;

@RequiredArgsConstructor
@Service
public class AuthorityService {

    private final AuthorityRepository authorityRepository;

    private final ModelMapper modelMapper;

    public List<AuthorityDto.Response> getAuthorities(AuthorityType authorityType) {
        return authorityRepository.findByAuthorityType(authorityType)
                .orElseThrow(() -> new AuthorityException(NOT_EXIST_AUTHORITY))
                .stream()
                .map(a -> modelMapper.map(a, AuthorityDto.Response.class))
                .collect(Collectors.toList());
    }
}
