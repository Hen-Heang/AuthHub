// Add GlobalExceptionHandler
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
}