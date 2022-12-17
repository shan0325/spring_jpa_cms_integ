package com.spring.cms.config.security;

import com.spring.cms.domain.Manager;
import com.spring.cms.dto.TokenDto;
import com.spring.cms.exception.JwtTokenException;
import com.spring.cms.exception.ManagerException;
import com.spring.cms.repository.ManagerRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.spring.cms.exception.JwtTokenException.JwtTokenExceptionType.*;
import static com.spring.cms.exception.ManagerException.ManagerExceptionType.NOT_FOUND_MANAGER;

/**
 * JWT 토큰에 관련된 암호화, 복호화, 검증 로직은 다 이곳에서 이루어집니다.
 * 생성자
 * application.yml 에 정의해놓은 jwt.secret 값을 가져와서 JWT 를 만들 때 사용하는 암호화 키값을 생성합니다.
 *
 * generateTokenDto
 * 유저 정보를 넘겨받아서 Access Token 과 Refresh Token 을 생성합니다.
 * 넘겨받은 유저 정보의 authentication.getName() 메소드가 username 을 가져옵니다.
 * 저는 username 으로 Member ID 를 저장했기 때문에 해당 값이 설정될 겁니다.
 * Access Token 에는 유저와 권한 정보를 담고 Refresh Token 에는 아무 정보도 담지 않습니다.
 *
 * getAuthentication
 * JWT 토큰을 복호화하여 토큰에 들어 있는 정보를 꺼냅니다.
 * Access Token 에만 유저 정보를 담기 때문에 명시적으로 accessToken 을 파라미터로 받게 했습니다.
 * Refresh Token 에는 아무런 정보 없이 만료일자만 담았습니다.
 * UserDetails 객체를 생생성해서 UsernamePasswordAuthenticationToken 형태로 리턴하는데 SecurityContext 를 사용하기 위한 절차라고 생각하면 됩니다..
 * 사실 좀 불필요한 절차라고 생각되지만 SecurityContext 가 Authentication 객체를 저장하기 때문에 어쩔수 없습니다.
 * parseClaims 메소드는 만료된 토큰이어도 정보를 꺼내기 위해서 따로 분리했습니다.
 *
 * validateToken
 * 토큰 정보를 검증합니다.
 * Jwts 모듈이 알아서 Exception 을 던져줍니다.
 */

@Slf4j
@Component
public class JwtProvider {

    public static final String AUTHORITIES_KEY = "auth";
    public static final String BEARER_TYPE = "bearer";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final int ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;            // 30분
    //public static final int REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;  // 7일
    public static final int REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60;  // 60분

    private final Key key;

    public JwtProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenDto.Generate generateTokenDto(Authentication authentication) {
        // 권한들 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();

        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())       // payload "sub": "name"
                .claim(AUTHORITIES_KEY, authorities)        // payload "auth": "ROLE_USER"
                .setExpiration(accessTokenExpiresIn)        // payload "exp": 1516239022 (예시)
                .signWith(key, SignatureAlgorithm.HS512)    // header "alg": "HS512"
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setSubject(authentication.getName())
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        return TokenDto.Generate.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExpiresIn.getTime())
                .refreshToken(refreshToken)
                .build();
    }

    public Authentication getAuthenticationByAccessToken(String token) {
        // 토큰 복호화
        Claims claims = parseClaims(token);

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new JwtTokenException(JWT_WITHOUT_AUTHORITY_INFO);
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public Authentication getAuthenticationByRefreshToken(String refreshToken, ManagerRepository managerRepository) {
        // 토큰 복호화 및 사용자 구하기
        String username = parseClaims(refreshToken).getSubject();
        Manager manager = managerRepository.findByUsername(username)
                .orElseThrow(() -> new ManagerException(NOT_FOUND_MANAGER));

        Collection<? extends GrantedAuthority> authorities = manager.getManagerAuthorities().stream()
                .map(m -> new SimpleGrantedAuthority(m.getAuthority().getAuthority()))
                .collect(Collectors.toList());

        UserDetails principal = new User(manager.getUsername(), manager.getPassword(), authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public void validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        } catch (SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
            throw new JwtTokenException(MALFORMED_JWT);
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
            throw new JwtTokenException(EXPIRED_JWT);
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
            throw new JwtTokenException(UNSUPPORTED_JWT);
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
            throw new JwtTokenException(ILLEGAL_ARGUMENT_JWT);
        }
    }

    // Request Header 에서 토큰 정보를 꺼내오기
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
