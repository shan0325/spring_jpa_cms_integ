package com.spring.cms.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class VersionInfoException extends BaseException {
    private BaseExceptionType baseExceptionType;

    @Getter
    @AllArgsConstructor
    public enum MenuExceptionType implements BaseExceptionType {
        NOT_FOUND_VERSION_INFO(HttpStatus.BAD_REQUEST, null, "버전 정보가 없습니다.");

        private HttpStatus httpStatus;
        private String errorCode;
        private String errorMessage;
    }
}
