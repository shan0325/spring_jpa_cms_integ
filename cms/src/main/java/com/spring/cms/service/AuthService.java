package com.spring.cms.service;

import com.spring.cms.dto.ManagerDto;
import com.spring.cms.dto.TokenDto;

import javax.servlet.http.HttpServletRequest;

public interface AuthService {
    TokenDto.Generate login(HttpServletRequest request, ManagerDto.Login login);
    TokenDto.Generate silentReissue(HttpServletRequest request, String refreshToken);
}
