package com.shan.spring.cms.service.impl;

import com.shan.spring.cms.dto.AuthorityDto;
import com.shan.spring.cms.enums.AuthorityTypeEnum;
import com.shan.spring.cms.exception.AuthorityException;
import com.shan.spring.cms.repository.AuthorityRepository;
import com.shan.spring.cms.service.AuthorityService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AuthorityServiceImpl implements AuthorityService {

    private final AuthorityRepository authorityRepository;

    private final ModelMapper modelMapper;

    @Override
    public List<AuthorityDto.Response> getAuthorities(AuthorityTypeEnum authorityType) {
        return authorityRepository.findByAuthorityType(authorityType)
                .orElseThrow(() -> new AuthorityException(AuthorityException.AuthorityExceptionType.NOT_EXIST_AUTHORITY))
                .stream()
                .map(a -> modelMapper.map(a, AuthorityDto.Response.class))
                .collect(Collectors.toList());
    }
}
