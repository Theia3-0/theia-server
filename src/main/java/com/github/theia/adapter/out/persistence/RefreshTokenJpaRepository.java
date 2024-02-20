package com.github.theia.adapter.out.persistence;

import com.github.theia.domain.refresh.RefreshTokenRedisEntity;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenJpaRepository extends CrudRepository<RefreshTokenRedisEntity, String> {
}
