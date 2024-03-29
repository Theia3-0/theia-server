package com.github.theia.global.error.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TheiaException extends RuntimeException{
    private final ErrorCode errorCode;
}
