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

    NOT_FOUND_EMAIL(404, "Not Found Email"),
    UNAUTHORIZATION_EMAIL(401, "Unauthorization Email"),
    NOT_VERIFY_CODE(400, "Not Verify Code"),
    MANY_EMAIL(400, "Many Mail Request"),

    ERROR_S3(500, "S3 Error"),
    ERROR_EMAIL(500, "Email Send Error"),
    ERROR_CODE(500, "Random Code Error"),
    ERROR_FEIGN(400, "Kakao Feign Error");

    private final int status;
    private final String message;
}
