package com.github.theia.application.auth.port.out;

import com.github.theia.domain.user.UserEntity;

import java.util.Optional;

public interface LoadUserByUserEmailPort {
    Optional<UserEntity> findByUserEmail(String email);
}
