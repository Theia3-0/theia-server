package com.github.theia.global.security.principle;

import com.github.theia.application.auth.port.out.LoadUserByUserEmailPort;
import com.github.theia.domain.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthDetailsService implements UserDetailsService {

    private final LoadUserByUserEmailPort loadUserByUserEmailPort;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = loadUserByUserEmailPort.findByUserEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("이메일을 찾을 수 없습니다."));
        return new AuthDetails(user);
    }
}