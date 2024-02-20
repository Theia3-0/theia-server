package com.github.theia.facade;

import com.github.theia.application.auth.port.out.LoadUserByUserEmailPort;
import com.github.theia.domain.user.UserEntity;
import com.github.theia.global.error.exception.TheiaException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;

import static com.github.theia.global.error.exception.ErrorCode.*;

@Configuration
@RequiredArgsConstructor
public class UserFacade {
    private final LoadUserByUserEmailPort loadUserByUserEmailPort;

    public UserEntity getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return getUserByEmail(email);
    }

    public UserEntity getUserByEmail(String email) {
        return loadUserByUserEmailPort.findByUserEmail(email)
                .orElseThrow(() -> new TheiaException(NOT_FOUND_USER));
    }
}
