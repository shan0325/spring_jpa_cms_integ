package com.spring.cms.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class CodeException extends BaseException {
    private BaseExceptionType baseExceptionType;

    @Getter
    @AllArgsConstructor
    public enum CodeExceptionType implements BaseExceptionType {
        NOT_FOUND_PARENT_CODE(HttpStatus.BAD_REQUEST, null,"상위 코드 정보가 없습니다."),
        NOT_FOUND_TOP_CODE(HttpStatus.BAD_REQUEST, null,"최상위 코드 정보가 없습니다."),
        NOT_FOUND_CODE(HttpStatus.BAD_REQUEST, null,"코드 정보가 없습니다."),
        CHILD_CODE_EXISTS_CANNOT_DELETE(HttpStatus.BAD_REQUEST, null,"하위 코드가 존재하여 삭제할 수 없습니다.");

        private HttpStatus httpStatus;
        private String errorCode;
        private String errorMessage;
    }
}
