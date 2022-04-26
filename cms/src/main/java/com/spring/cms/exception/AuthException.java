package com.spring.cms.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class AuthException extends BaseException {
    private BaseExceptionType baseExceptionType;

    @Getter
    @AllArgsConstructor
    public enum AuthExceptionType implements BaseExceptionType {
        INVALID_AUTHORIZATION(HttpStatus.BAD_REQUEST, "INVALID_AUTHORIZATIOIN", "Authorization 값이 유효하지 않습니다."),
        INVALID_ACCESS_TOKEN(HttpStatus.BAD_REQUEST, "INVALID_ACCESS_TOKEN", "accessToken 값이 유효하지 않습니다."),
        INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "INVALID_REFRESH_TOKEN", "refreshToken 값이 유효하지 않습니다.");

        private HttpStatus httpStatus;
        private String errorCode;
        private String errorMessage;
    }
}
