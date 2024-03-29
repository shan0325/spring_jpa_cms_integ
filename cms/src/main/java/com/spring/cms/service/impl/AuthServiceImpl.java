package com.spring.cms.service.impl;

import com.spring.cms.config.security.JwtProvider;
import com.spring.cms.domain.Manager;
import com.spring.cms.domain.ManagerAuthority;
import com.spring.cms.domain.RefreshToken;
import com.spring.cms.dto.ManagerDto;
import com.spring.cms.dto.TokenDto;
import com.spring.cms.exception.AuthException;
import com.spring.cms.exception.ManagerException;
import com.spring.cms.repository.ManagerRepository;
import com.spring.cms.repository.RefreshTokenRepository;
import com.spring.cms.service.AuthService;
import com.spring.cms.util.HelperUtils;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.spring.cms.exception.AuthException.AuthExceptionType.*;
import static com.spring.cms.exception.ManagerException.ManagerExceptionType.NOT_FOUND_MANAGER;

/**
 *#로그인 (login)
 *
 * Authentication
 * 사용자가 입력한 Login ID, PW 로 인증 정보 객체 UsernamePasswordAuthenticationToken를 생성합니다.
 * 아직 인증이 완료된 객체가 아니며 AuthenticationManager 에서 authenticate 메소드의 파라미터로 넘겨서 검증 후에 Authentication 를 받습니다.
 * AuthenticationManager
 * 스프링 시큐리티에서 실제로 인증이 이루어지는 곳입니다.
 * authenticate 메소드 하나만 정의되어 있는 인터페이스며 위 코드에서는 Builder 에서 UserDetails 의 유저 정보가 서로 일치하는지 검사합니다.
 * 그런데 코드상으로는 전혀 구현된게 없는데 어떻게 된 걸까요?
 * 내부적으로 수행되는 검증 과정은 아래의 CustomUserDetailsService 클래스에서 다루겠습니다.
 * 인증이 완료된 authentication 에는 Member ID 가 들어있습니다.
 * 인증 객체를 바탕으로 Access Token + Refresh Token 을 생성합니다.
 * Refresh Token 은 저장하고, 생성된 토큰 정보를 클라이언트에게 전달합니다.
 *
 * #재발급 (reissue)
 *
 * Access Token + Refresh Token 을 Request Body 에 받아서 검증합니다.
 * Refresh Token 의 만료 여부를 먼저 검사합니다.
 * Access Token 을 복호화하여 유저 정보 (Member ID) 를 가져오고 저장소에 있는 Refresh Token 과 클라이언트가 전달한 Refresh Token 의 일치 여부를 검사합니다.
 * 만약 일치한다면 로그인 했을 때와 동일하게 새로운 토큰을 생성해서 클라이언트에게 전달합니다.
 * Refresh Token 은 재사용하지 못하게 저장소에서 값을 갱신해줍니다.
 */

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ManagerRepository managerRepository;

    @Override
    public TokenDto.Generate login(HttpServletRequest request, ManagerDto.Login login) {
        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword());

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto.Generate tokenDto = jwtProvider.generateTokenDto(authentication);
        log.info("login jwtToken : " + tokenDto);

        // 4. RefreshToken 저장
        Optional<RefreshToken> findRefreshTokenOptional = refreshTokenRepository.findByKey(authentication.getName());
        if (findRefreshTokenOptional.isPresent()) {
            findRefreshTokenOptional.get().updateValue(tokenDto.getRefreshToken(), HelperUtils.getClientIp(request));
        } else {
            refreshTokenRepository.save(RefreshToken.builder()
                    .key(authentication.getName())
                    .value(tokenDto.getRefreshToken())
                    .ip(HelperUtils.getClientIp(request))
                    .build());
        }

        // 5. 토큰 발급
        return tokenDto;
    }

    @Override
    public TokenDto.Generate silentReissue(HttpServletRequest request, String refreshToken) {
        // 1. Refresh Token 검증
        jwtProvider.validateToken(refreshToken);

        // 2. Refresh Token 에서 ID 가져오기
        Authentication authentication = jwtProvider.getAuthenticationByRefreshToken(refreshToken, managerRepository);

        // 3. 저장소에서 ID 를 기반으로 Refresh Token 값 가져옴
        RefreshToken findRefreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new AuthException(NOT_FOUND_REFRESH_TOKEN));

        // 4. 로그인한 ip와 같은지 확인
        String currentClientIp = HelperUtils.getClientIp(request);
        if (!findRefreshToken.getIp().equals(currentClientIp)) {
            throw new AuthException(NOT_MATCH_LOGIN_IP);
        }

        // 5. Refresh Token 일치하는지 검사
        if (!findRefreshToken.getValue().equals(refreshToken)) {
            log.info("refreshToken : " + refreshToken + "\nfindRefreshToken : " + findRefreshToken.getValue());
            throw new AuthException(NOT_MATCH_TOKEN);
        }

        // 6. 새로운 토큰 생성
        TokenDto.Generate tokenDto = jwtProvider.generateTokenDto(authentication);
        log.info("silentReissue jwtToken : " + tokenDto);

        // 7. 저장소 정보 업데이트
        findRefreshToken.updateValue(tokenDto.getRefreshToken(), HelperUtils.getClientIp(request));

        return tokenDto;
    }
}
