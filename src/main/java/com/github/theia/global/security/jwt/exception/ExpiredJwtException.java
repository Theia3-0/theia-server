package com.github.theia.global.security.jwt.exception;


import com.github.theia.global.error.exception.ErrorCode;
import com.github.theia.global.error.exception.TheiaException;

public class ExpiredJwtException extends TheiaException {

    public static final TheiaException EXCEPTION =
            new ExpiredJwtException();

    private ExpiredJwtException() {
        super(ErrorCode.EXPIRED_JWT);
    }
}