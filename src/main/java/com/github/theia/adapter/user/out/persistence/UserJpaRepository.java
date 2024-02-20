package com.github.theia.adapter.user.out.persistence;

import com.github.theia.domain.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUserEmail(String email);
    boolean existsByUserEmail(String email);
    boolean existsByUserName(String name);
}
