package com.spring.cms.service;

import com.spring.cms.domain.Manager;
import com.spring.cms.dto.ManagerDto;


public interface ManagerService {
    ManagerDto.AuthResponse getAuthManager(String username);

    Manager getManagerByManagerId(Long managerId);
}
