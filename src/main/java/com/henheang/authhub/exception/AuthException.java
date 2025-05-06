package com.henheang.authhub.exception;

import com.henheang.authhub.common.api.ExitCode;
import lombok.Getter;

@Getter
public class AuthException extends RuntimeException {
    private final ExitCode exitCode;

    public AuthException(ExitCode exitCode) {
        super(exitCode.getMessage());
        this.exitCode = exitCode;
    }

    public AuthException(ExitCode exitCode, String customMessage) {
        super(customMessage);
        this.exitCode = exitCode;
    }
}