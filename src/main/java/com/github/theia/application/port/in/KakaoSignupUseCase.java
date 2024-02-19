package com.github.theia.application.port.in;

import com.github.theia.adapter.in.rest.dto.request.UserSignupRequest;
import com.github.theia.domain.user.UserEntity;

public interface KakaoSignupUseCase {
    void signup(UserSignupRequest userSignupRequest, UserEntity user);
}
