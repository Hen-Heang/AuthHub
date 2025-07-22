package com.henheang.securityapi.exception;


import com.henheang.commonapi.components.common.api.ApiResponse;
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
        ApiResponse<Object> apiResponse = ApiResponse.error(
                ExitCode.SYSTEM_ERROR,
                ex.getMessage()
        );

        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadRequestException(BadRequestException ex, WebRequest request) {
        ApiResponse<Object> apiResponse = ApiResponse.error(
                ExitCode.SYSTEM_ERROR,
                ex.getMessage()
        );

        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Object>> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        ApiResponse<Object> apiResponse = ApiResponse.error(
                ExitCode.AUTHENTICATION_FAILED,
                "Authentication failed: " + ex.getMessage()
        );

        return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
        ApiResponse<Object> apiResponse = ApiResponse.error(
                ExitCode.INVALID_CREDENTIALS,
                "Invalid email or password"
        );

        return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        ApiResponse<Object> apiResponse = ApiResponse.error(
                ExitCode.INSUFFICIENT_PERMISSIONS,
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

        ApiResponse<Object> apiResponse = ApiResponse.error(
                ExitCode.SYSTEM_ERROR,
                "Validation failed",
                errors
        );

        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGlobalException(Exception ex, WebRequest request) {
        ApiResponse<Object> apiResponse = ApiResponse.error(
                ExitCode.SYSTEM_ERROR,
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

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ApiResponse<Object>> handleAuthException(AuthException ex, WebRequest request) {
        ApiResponse<Object> apiResponse = ApiResponse.error(
                ex.getExitCode(),
                ex.getMessage()
        );

        return new ResponseEntity<>(apiResponse, getHttpStatusForExitCode(ex.getExitCode()));
    }

    private HttpStatus getHttpStatusForExitCode(ExitCode exitCode) {
        int code = exitCode.getCode();

        if (code >= 1000 && code < 1100) {
            return HttpStatus.UNAUTHORIZED; // Authentication errors
        } else if (code >= 1100 && code < 1200) {
            return HttpStatus.BAD_REQUEST; // Registration errors
        } else if (code >= 1200 && code < 1300) {
            return HttpStatus.BAD_REQUEST; // Password reset errors
        } else if (code >= 1300 && code < 1400) {
            return HttpStatus.BAD_REQUEST; // OAuth errors
        } else if (code >= 5000 && code < 6000) {
            return HttpStatus.INTERNAL_SERVER_ERROR; // System errors
        } else {
            return HttpStatus.INTERNAL_SERVER_ERROR; // Default for unknown errors
        }
    }
}