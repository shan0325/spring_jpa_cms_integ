package com.shan.spring.cms.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ManagerException extends BaseException {
    private BaseExceptionType baseExceptionType;

    public ManagerException(String message) {
        super(message);
    }

    @Getter
    @AllArgsConstructor
    public enum ManagerExceptionType implements BaseExceptionType {
        ALREADY_EXIST_MANAGER(HttpStatus.BAD_REQUEST, null, "이미 존재하는 계정입니다."),
        NOT_FOUND_MANAGER(HttpStatus.BAD_REQUEST, null, "관리자 정보가 없습니다.");

        private HttpStatus httpStatus;
        private String errorCode;
        private String errorMessage;
    }
}
