package com.spring.cms.controller;


import com.spring.cms.config.security.JwtProvider;
import com.spring.cms.dto.ManagerDto;
import com.spring.cms.dto.TokenDto;
import com.spring.cms.service.AuthService;
import com.spring.cms.util.CookieUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 로그인 구현 참고 URL
 * https://velog.io/@yaytomato/%ED%94%84%EB%A1%A0%ED%8A%B8%EC%97%90%EC%84%9C-%EC%95%88%EC%A0%84%ED%95%98%EA%B2%8C-%EB%A1%9C%EA%B7%B8%EC%9D%B8-%EC%B2%98%EB%A6%AC%ED%95%98%EA%B8%B0
 */

@RequiredArgsConstructor
@RestController
@RequestMapping(RestControllerBase.API_URI_PREFIX + "/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(HttpServletRequest request, HttpServletResponse response,
                                   @RequestBody @Valid ManagerDto.Login login) {

        TokenDto.Generate generate = authService.login(request, login);

        // refreshToken은 서버에서 쿠키 저장(HttpOnly 설정하기 위함)
        CookieUtils.addCookie(response, "refreshToken", generate.getRefreshToken(), JwtProvider.REFRESH_TOKEN_EXPIRE_TIME / 1000);

        return ResponseEntity.ok(generate);
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {

        // refreshToken 쿠키 삭제
        CookieUtils.deleteCookie(request, response, "refreshToken");

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/silentReissue")
    public ResponseEntity<?> silentReissue(HttpServletRequest request, HttpServletResponse response,
                                           @CookieValue(value = "refreshToken", required = false) String refreshToken) {

        TokenDto.Generate generate = authService.silentReissue(request, refreshToken);

        // refreshToken은 서버에서 쿠키 저장(HttpOnly 설정하기 위함)
        CookieUtils.addCookie(response, "refreshToken", generate.getRefreshToken(), JwtProvider.REFRESH_TOKEN_EXPIRE_TIME / 1000);

        return ResponseEntity.ok(generate);
    }
}
