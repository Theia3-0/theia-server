package com.github.theia.application.auth.port.out;

import com.github.theia.domain.user.UserEntity;

public interface SaveUserPort {
    UserEntity save(UserEntity user);
}
