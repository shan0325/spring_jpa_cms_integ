package com.shan.spring.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.shan.spring.error.apiError.ApiError;
import com.shan.spring.cms.exception.BaseExceptionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.shan.spring.cms.exception.JwtTokenException.JwtTokenExceptionType.ILLEGAL_ARGUMENT_JWT;

/**
 * 유저 정보 없이 접근하면 SC_UNAUTHORIZED (401) 응답을 내려줍니다.
 */

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        log.error("Unauthorized error: {}", authException.getMessage());

        BaseExceptionType exceptionType = (BaseExceptionType) request.getAttribute("EXCEPTION_TYPE");
        if (exceptionType == null) {
            exceptionType = ILLEGAL_ARGUMENT_JWT;
        }
        setResponse(response, exceptionType);
    }

    private void setResponse(HttpServletResponse response, BaseExceptionType exceptionType) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 유효한 자격증명을 제공하지 않고 접근하려 할때 401

        ApiError apiError = new ApiError(exceptionType.getHttpStatus(),
                exceptionType.getErrorCode(),
                exceptionType.getErrorMessage());

        response.getWriter().print(new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .writeValueAsString(apiError));
    }
}
