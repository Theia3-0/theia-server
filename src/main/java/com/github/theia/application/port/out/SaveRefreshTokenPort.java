package com.github.theia.application.port.out;

import com.github.theia.domain.refresh.RefreshTokenRedisEntitry;

public interface SaveRefreshTokenPort {
    RefreshTokenRedisEntitry save(RefreshTokenRedisEntitry refreshTokenRedisEntitry);
}
