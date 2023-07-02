package com.shan.spring.cms.service;

import com.shan.spring.cms.dto.ManagerDto;
import com.shan.spring.cms.dto.TokenDto;

import javax.servlet.http.HttpServletRequest;

public interface AuthService {
    TokenDto.Generate login(HttpServletRequest request, ManagerDto.Login login);
    TokenDto.Generate silentReissue(HttpServletRequest request, String refreshToken);
}
