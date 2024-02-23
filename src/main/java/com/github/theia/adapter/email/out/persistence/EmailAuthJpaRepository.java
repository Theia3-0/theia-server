package com.github.theia.adapter.email.out.persistence;

import com.github.theia.domain.email.EmailAuthRedisEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface EmailAuthJpaRepository extends CrudRepository<EmailAuthRedisEntity, String> {
    Optional<EmailAuthRedisEntity> findByEmail(String email);
}
