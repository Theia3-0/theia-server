package com.github.theia.application.email.port.in;

public interface VerifyEmailUseCase {
    void verifyEmail(String email, String code);
}
