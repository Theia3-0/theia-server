package com.github.theia.adapter.email.out.persistence;

import com.github.theia.domain.email.EmailAuthRedisEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailAuthJpaRepository extends JpaRepository<EmailAuthRedisEntity, String> {
}
