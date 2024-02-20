package com.github.theia.adapter.user.out.persistence;

import com.github.theia.application.auth.port.out.IsUserByEmailPort;
import com.github.theia.application.auth.port.out.IsUserByNamePort;
import com.github.theia.application.auth.port.out.LoadUserByUserEmailPort;
import com.github.theia.application.auth.port.out.SaveUserPort;
import com.github.theia.domain.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository implements LoadUserByUserEmailPort, SaveUserPort, IsUserByEmailPort, IsUserByNamePort {

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

    @Override
    public boolean isUserByName(String name) {
        return userJpaRepository.existsByUserName(name);
    }
}
