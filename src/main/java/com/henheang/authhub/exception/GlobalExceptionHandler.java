package com.henheang.authhub.exception;

import com.henheang.authhub.common.api.ApiResponse;
import com.henheang.authhub.common.api.ApiStatus;
import com.henheang.authhub.common.api.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ApiResponse<Object> apiResponse = new ApiResponse<>(
                new ApiStatus(StatusCode.NOT_FOUND),
                ex.getMessage()
        );

        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadRequestException(BadRequestException ex, WebRequest request) {
        ApiResponse<Object> apiResponse = new ApiResponse<>(
                new ApiStatus(StatusCode.BAD_REQUEST),
                ex.getMessage()
        );

        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Object>> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        ApiResponse<Object> apiResponse = new ApiResponse<>(
                new ApiStatus(StatusCode.UNAUTHORIZED),
                "Authentication failed: " + ex.getMessage()
        );

        return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
        ApiResponse<Object> apiResponse = new ApiResponse<>(
                new ApiStatus(StatusCode.BAD_CREDENTIAL),
                "Invalid email or password"
        );

        return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        ApiResponse<Object> apiResponse = new ApiResponse<>(
                new ApiStatus(StatusCode.FORBIDDEN),
                "You don't have permission to access this resource"
        );

        return new ResponseEntity<>(apiResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ApiResponse<Object> apiResponse = new ApiResponse<>(
                new ApiStatus(StatusCode.BAD_REQUEST),
                errors
        );

        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGlobalException(Exception ex, WebRequest request) {
        ApiResponse<Object> apiResponse = new ApiResponse<>(
                new ApiStatus(StatusCode.BAD_GATEWAY),
                "An unexpected error occurred: " + ex.getMessage()
        );

        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Object>> handleBusinessException(BusinessException ex, WebRequest request) {
        ApiResponse<Object> apiResponse = new ApiResponse<>(
                new ApiStatus(ex.getErrorCode()),
                ex.getBody() != null ? ex.getBody() : ex.getMessage()
        );

        return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(ex.getErrorCode().getHttpCode()));
    }
}