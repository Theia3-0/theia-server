package com.github.theia.adapter.out.persistence;

import com.github.theia.application.port.out.SaveRefreshTokenPort;
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
