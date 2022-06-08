package com.spring.cms.service;

import com.spring.cms.dto.ManagerDto;
import com.spring.cms.dto.MemberDto;
import com.spring.cms.exception.ManagerException;
import com.spring.cms.exception.MemberException;
import com.spring.cms.repository.ManagerRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.spring.cms.exception.ManagerException.ManagerExceptionType.NOT_FOUND_MANAGER;
import static com.spring.cms.exception.MemberException.MemberExceptionType.NOT_FOUND_MEMBER;


@Transactional(readOnly = false)
@RequiredArgsConstructor
@Service
public class ManagerService {

    private final ManagerRepository managerRepository;
    private final ModelMapper modelMapper;

    public ManagerDto.AuthResponse getAuthManager(String username) {
        return managerRepository.findByUsername(username)
                .map(u -> modelMapper.map(u, ManagerDto.AuthResponse.class))
                .orElseThrow(() -> new ManagerException(NOT_FOUND_MANAGER));
    }
}
