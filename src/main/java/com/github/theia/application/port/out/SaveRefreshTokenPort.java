package com.github.theia.application.port.out;

import com.github.theia.domain.refresh.RefreshTokenRedisEntity;

public interface SaveRefreshTokenPort {
    RefreshTokenRedisEntity save(RefreshTokenRedisEntity refreshTokenRedisEntity);
}
