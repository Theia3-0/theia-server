package com.github.theia.application.email.port.out;

import com.github.theia.domain.email.EmailAuthRedisEntity;

import java.util.Optional;

public interface LoadEmailAuthByEmailPort {
    Optional<EmailAuthRedisEntity> findByEmail(String email);
}
