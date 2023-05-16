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
        INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "INVALID_ACCESS_TOKEN", "accessToken 값이 유효하지 않습니다."),
        INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "INVALID_REFRESH_TOKEN", "refreshToken 값이 유효하지 않습니다."),
        NOT_MATCH_TOKEN(HttpStatus.BAD_REQUEST, "NOT_MATCH_TOKEN", "토큰의 유저 정보가 일치하지 않습니다."),
        NOT_FOUND_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "NOT_FOUND_REFRESH_TOKEN", "refreshToken 정보가 없습니다."),
        NOT_MATCH_LOGIN_IP(HttpStatus.BAD_REQUEST, "NOT_MATCH_LOGIN_IP", "로그인한 IP와 요청한 IP가 다릅니다.");

        private HttpStatus httpStatus;
        private String errorCode;
        private String errorMessage;
    }
}
