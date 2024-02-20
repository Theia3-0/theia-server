package com.github.theia.adapter.auth.out.persistence;

import com.github.theia.application.auth.port.out.SaveRefreshTokenPort;
import com.github.theia.domain.refresh.RefreshTokenRedisEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository implements SaveRefreshTokenPort {

    private final RefreshTokenJpaRepository refreshTokenJpaRepository;

    @Override
    public RefreshTokenRedisEntity save(RefreshTokenRedisEntity refreshTokenRedisEntity) {
        return refreshTokenJpaRepository.save(refreshTokenRedisEntity);
    }
}
