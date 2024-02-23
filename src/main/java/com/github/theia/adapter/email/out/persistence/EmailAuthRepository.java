package com.github.theia.adapter.email.out.persistence;

import com.github.theia.application.email.port.out.SaveEmailPort;
import com.github.theia.domain.email.EmailAuthRedisEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EmailAuthRepository implements SaveEmailPort {

    private final EmailAuthJpaRepository emailAuthJpaRepository;

    @Override
    public EmailAuthRedisEntity save(EmailAuthRedisEntity emailAuthRedisEntity) {
        return emailAuthJpaRepository.save(emailAuthRedisEntity);
    }
}
