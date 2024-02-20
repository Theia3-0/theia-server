package com.github.theia.adapter.out.persistence;

import com.github.theia.application.port.out.SaveRefreshTokenPort;
import com.github.theia.domain.refresh.RefreshTokenRedisEntitry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository implements SaveRefreshTokenPort {

    private final RefreshTokenJpaRepository refreshTokenJpaRepository;

    @Override
    public RefreshTokenRedisEntitry save(RefreshTokenRedisEntitry refreshTokenRedisEntitry) {
        return refreshTokenJpaRepository.save(refreshTokenRedisEntitry);
    }
}
