package com.spring.cms.exception;

import com.spring.cms.error.apiError.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	/**
     * Handle MissingServletRequestParameterException. Triggered when a 'required' request parameter is missing.
     *
     * @param ex      MissingServletRequestParameterException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiError object
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        String error = ex.getParameterName() + "는(은) 필수 값입니다.";
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, ex));
    }


    /**
     * Handle HttpMediaTypeNotSupportedException. This one triggers when JSON is invalid as well.
     *
     * @param ex      HttpMediaTypeNotSupportedException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiError object
     */
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));
        return buildResponseEntity(new ApiError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, builder.substring(0, builder.length() - 2), ex));
    }

    /**
     * Handle MethodArgumentNotValidException. Triggered when an object fails @Valid validation.
     *
     * @param ex      the MethodArgumentNotValidException that is thrown when @Valid validation fails
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiError object
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, getFieldErrorMessage(ex));
        apiError.addValidationErrors(ex.getBindingResult().getFieldErrors());
        apiError.addValidationError(ex.getBindingResult().getGlobalErrors());
        return buildResponseEntity(apiError);
    }

    /**
     * Handle BindException. Triggered when an object fails @Valid validation.
     *
     * @param ex      the BindException that is thrown when @Valid validation fails
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiError object
     */
    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, getFieldErrorMessage(ex));
        apiError.addValidationErrors(ex.getBindingResult().getFieldErrors());
        apiError.addValidationError(ex.getBindingResult().getGlobalErrors());
        return buildResponseEntity(apiError);
	}

    private String getFieldErrorMessage(BindException ex) {
        String message = "유효하지 않은 값 입니다.";

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        if (fieldErrors != null && fieldErrors.size() > 0) {
            FieldError fieldError = fieldErrors.get(0);
            message = fieldError.getDefaultMessage();
        }
        return message;
    }

    /**
     * Handles javax.validation.ConstraintViolationException. Thrown when @Validated fails.
     *
     * @param ex the ConstraintViolationException
     * @return the ApiError object
     */
    @ExceptionHandler(javax.validation.ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(javax.validation.ConstraintViolationException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "유효하지 않은 값 입니다.");
        apiError.addValidationErrors(ex.getConstraintViolations());
        return buildResponseEntity(apiError);
    }

    /**
     * Handles EntityNotFoundException. Created to encapsulate errors with more detail than javax.persistence.EntityNotFoundException.
     *
     * @param ex the EntityNotFoundException
     * @return the ApiError object
     */
    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage());
        return buildResponseEntity(apiError);
    }

    /**
     * Handle HttpMessageNotReadableException. Happens when request JSON is malformed.
     *
     * @param ex      HttpMessageNotReadableException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiError object
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        log.info("{} to {}", servletWebRequest.getHttpMethod(), servletWebRequest.getRequest().getServletPath());
        String error = "Malformed JSON request";
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, ex));
    }

    /**
     * Handle HttpMessageNotWritableException.
     *
     * @param ex      HttpMessageNotWritableException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiError object
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "Error writing JSON output";
        return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, error, ex));
    }

    /**
     * Handle javax.persistence.EntityNotFoundException
     */
    @ExceptionHandler(javax.persistence.EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(javax.persistence.EntityNotFoundException ex) {
        return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND, ex));
    }

    /**
     * Handle DataIntegrityViolationException, inspects the cause for different DB causes.
     *
     * @param ex the DataIntegrityViolationException
     * @return the ApiError object
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex, WebRequest request) {
        if (ex.getCause() instanceof ConstraintViolationException) {
            return buildResponseEntity(new ApiError(HttpStatus.CONFLICT, "Database error", ex.getCause()));
        }
        return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex));
    }

    /**
     * Handle Exception, handle generic Exception.class
     *
     * @param ex the Exception
     * @return the ApiError object
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
        String message = String.format("The parameter '%s' of value '%s' could not be converted to type '%s'", ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, message, ex);
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleException(Exception ex) {
        return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex));
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<Object> handleBaseException(BaseException ex) {
        BaseExceptionType baseExceptionType = ex.getBaseExceptionType();
        return buildResponseEntity(new ApiError(baseExceptionType.getHttpStatus()
                , baseExceptionType.getErrorCode()
                , baseExceptionType.getErrorMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException ex) {
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, "비밀번호가 잘못되었습니다.", ex));
    }

	private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
		return new ResponseEntity<>(apiError, apiError.getStatus());
	}
}
