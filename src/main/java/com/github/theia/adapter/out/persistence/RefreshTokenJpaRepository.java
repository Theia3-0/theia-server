package com.github.theia.adapter.out.persistence;

import com.github.theia.domain.refresh.RefreshTokenRedisEntitry;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenJpaRepository extends CrudRepository<RefreshTokenRedisEntitry, String> {
}
