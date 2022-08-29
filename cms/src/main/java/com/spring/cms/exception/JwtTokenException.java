package com.spring.cms.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class JwtTokenException extends RuntimeException {
    private BaseExceptionType baseExceptionType;

    @Getter
    @AllArgsConstructor
    public enum JwtTokenExceptionType implements BaseExceptionType {
        MALFORMED_JWT(HttpStatus.UNAUTHORIZED, "MALFORMED_JWT", "잘못된 JWT 서명입니다."),
        EXPIRED_JWT(HttpStatus.UNAUTHORIZED, "EXPIRED_JWT", "만료된 JWT 토큰입니다."),
        UNSUPPORTED_JWT(HttpStatus.UNAUTHORIZED, "UNSUPPORTED_JWT", "지원되지 않는 JWT 토큰입니다."),
        ILLEGAL_ARGUMENT_JWT(HttpStatus.UNAUTHORIZED, "ILLEGAL_ARGUMENT_JWT", "JWT 토큰이 잘못되었습니다.");

        private HttpStatus httpStatus;
        private String errorCode;
        private String errorMessage;
    }
}