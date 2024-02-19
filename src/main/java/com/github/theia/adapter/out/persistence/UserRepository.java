package com.github.theia.adapter.out.persistence;

import com.github.theia.application.port.out.IsUserByEmailPort;
import com.github.theia.application.port.out.LoadUserByUserEmailPort;
import com.github.theia.application.port.out.SaveUserPort;
import com.github.theia.domain.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository implements LoadUserByUserEmailPort, SaveUserPort, IsUserByEmailPort {

    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<UserEntity> findByUserEmail(String email) {
        return userJpaRepository.findByUserEmail(email);
    }

    @Override
    public UserEntity save(UserEntity user) {
        return userJpaRepository.save(user);
    }

    @Override
    public boolean isUserByEmail(String email) {
        return userJpaRepository.existsByUserEmail(email);
    }
}
