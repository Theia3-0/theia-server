package com.github.theia.global.error;

import com.github.theia.global.error.exception.ErrorCode;
import com.github.theia.global.error.exception.ErrorResponse;
import com.github.theia.global.error.exception.TheiaException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 비즈니스 로직에서의 에러
    @ExceptionHandler(TheiaException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(TheiaException e) {

        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse response = ErrorResponse.of(errorCode, errorCode.getMessage());

        return new ResponseEntity<>(response, HttpStatus.valueOf(errorCode.getStatus()));
    }

}
