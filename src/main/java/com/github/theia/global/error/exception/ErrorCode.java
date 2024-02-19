package com.github.theia.global.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    EXPIRED_JWT(401, "Expired Jwt"),
    INVALID_JWT(401, "Invalid Jwt"),

    ERROR_FEIGN(500, "카카오 유저 정보를 불러오던 중 오류가 발생했습니다");

    private final int status;
    private final String message;
}
