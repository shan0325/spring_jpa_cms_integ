package com.spring.cms.controller;

import com.spring.cms.config.security.JwtProvider;
import com.spring.cms.dto.MemberDto;
import com.spring.cms.dto.TokenDto;
import com.spring.cms.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 로그인 구현 참고 URL
 * https://velog.io/@yaytomato/%ED%94%84%EB%A1%A0%ED%8A%B8%EC%97%90%EC%84%9C-%EC%95%88%EC%A0%84%ED%95%98%EA%B2%8C-%EB%A1%9C%EA%B7%B8%EC%9D%B8-%EC%B2%98%EB%A6%AC%ED%95%98%EA%B8%B0
 */

@RequiredArgsConstructor
@RestController
@RequestMapping(RestControllerBase.API_URI_PREFIX +  "/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenDto.Generate> login(@RequestBody @Valid MemberDto.Login login, HttpServletResponse response) {
        TokenDto.Generate generate = authService.login(login);

        // refreshToken은 서버에서 쿠키 저장(HttpOnly 설정하기 위함)
        Cookie cookie = new Cookie("refreshToken", generate.getRefreshToken());
        cookie.setPath("/");
        cookie.setMaxAge(JwtProvider.REFRESH_TOKEN_EXPIRE_TIME / 1000);
        //cookie.setSecure(true);
        cookie.setHttpOnly(true); // xss 방지를 위해 설정

        response.addCookie(cookie);

        return ResponseEntity.ok(generate);
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenDto.Generate> reissue(@RequestBody @Valid TokenDto.Reissue reissue) {
        return ResponseEntity.ok(authService.reissue(reissue));
    }

    @PostMapping("/silentReissue")
    public ResponseEntity<TokenDto.Generate> silentReissue(@CookieValue(name = "refreshToken") String refreshToken) {
        return ResponseEntity.ok(authService.silentReissue(refreshToken));
    }
}
