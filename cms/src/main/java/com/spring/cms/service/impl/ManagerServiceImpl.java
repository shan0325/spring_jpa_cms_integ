package com.spring.cms.service.impl;

import com.spring.cms.domain.Manager;
import com.spring.cms.dto.ManagerDto;
import com.spring.cms.exception.ManagerException;
import com.spring.cms.repository.ManagerRepository;
import com.spring.cms.service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.spring.cms.exception.ManagerException.ManagerExceptionType.NOT_FOUND_MANAGER;


@Transactional(readOnly = false)
@RequiredArgsConstructor
@Service
public class ManagerServiceImpl implements ManagerService {

    private final ManagerRepository managerRepository;
    private final ModelMapper modelMapper;

    @Override
    public ManagerDto.AuthResponse getAuthManager(String username) {
        return managerRepository.findByUsername(username)
                .map(u -> modelMapper.map(u, ManagerDto.AuthResponse.class))
                .orElseThrow(() -> new ManagerException(NOT_FOUND_MANAGER));
    }

    @Override
    public Manager getManagerByManagerId(Long managerId) {
        return managerRepository.findById(managerId)
                .orElseThrow(() -> new ManagerException(NOT_FOUND_MANAGER));
    }
}
