package com.spring.cms.service;

import com.spring.cms.dto.ManagerDto;
import com.spring.cms.dto.TokenDto;

public interface AuthService {
    TokenDto.Generate login(ManagerDto.Login login);
    TokenDto.Generate silentReissue(String refreshToken);
}
