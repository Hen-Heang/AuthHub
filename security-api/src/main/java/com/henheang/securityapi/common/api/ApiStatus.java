package com.henheang.securityapi.common.api;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApiStatus {
    private int code;
    private String message;

    public ApiStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ApiStatus(StatusCode statusCode) {
        this.code = statusCode.getCode();
        this.message = statusCode.getMessage();
    }

    public ApiStatus(ExitCode exitCode) {
        this.code = exitCode.getCode();
        this.message = exitCode.getMessage();
    }
}