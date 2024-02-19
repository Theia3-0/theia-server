package com.github.theia.adapter.out.persistence;

import com.github.theia.application.port.out.LoadUserByUserEmailPort;
import com.github.theia.domain.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository implements LoadUserByUserEmailPort {

    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<UserEntity> findByUserEmail(String email) {
        return userJpaRepository.findByUserEmail(email);
    }
}
