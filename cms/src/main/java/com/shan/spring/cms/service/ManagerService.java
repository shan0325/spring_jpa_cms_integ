package com.shan.spring.cms.service;

import com.shan.spring.cms.dto.ManagerDto;
import com.shan.spring.cms.domain.Manager;


public interface ManagerService {
    ManagerDto.AuthResponse getAuthManager(String username);

    Manager getManagerByManagerId(Long managerId);
}
