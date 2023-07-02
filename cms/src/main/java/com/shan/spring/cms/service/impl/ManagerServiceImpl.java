package com.shan.spring.cms.service.impl;

import com.shan.spring.cms.dto.ManagerDto;
import com.shan.spring.cms.exception.ManagerException;
import com.shan.spring.cms.repository.ManagerRepository;
import com.shan.spring.cms.domain.Manager;
import com.shan.spring.cms.service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
                .orElseThrow(() -> new ManagerException(ManagerException.ManagerExceptionType.NOT_FOUND_MANAGER));
    }

    @Override
    public Manager getManagerByManagerId(Long managerId) {
        return managerRepository.findById(managerId)
                .orElseThrow(() -> new ManagerException(ManagerException.ManagerExceptionType.NOT_FOUND_MANAGER));
    }
}
