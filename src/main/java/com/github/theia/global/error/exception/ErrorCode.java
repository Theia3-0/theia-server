package com.github.theia.global.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    EXPIRED_JWT(401, "Expired Jwt"),
    INVALID_JWT(401, "Invalid Jwt"),
    DUPLICATE_USER(400, "Duplicated User"),
    NOT_FOUND_USER(404, "Not Found User"),

    ERROR_FEIGN(400, "Kakao Feign Error");

    private final int status;
    private final String message;
}
