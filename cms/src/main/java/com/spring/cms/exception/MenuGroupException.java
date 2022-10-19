package com.spring.cms.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class MenuGroupException extends BaseException {
    private BaseExceptionType baseExceptionType;

    @Getter
    @AllArgsConstructor
    public enum MenuGroupExceptionType implements BaseExceptionType {
        NOT_FOUND_MENU_GROUP(HttpStatus.BAD_REQUEST, null,"메뉴 그룹 정보가 없습니다.");

        private HttpStatus httpStatus;
        private String errorCode;
        private String errorMessage;
    }
}
