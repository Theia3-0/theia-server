package com.github.theia.application.email.port.out;

import com.github.theia.domain.email.EmailAuthRedisEntity;

public interface SaveEmailPort {
    EmailAuthRedisEntity save(EmailAuthRedisEntity emailAuthRedisEntity);
}
