package com.github.theia.application.auth.port.in;

import com.github.theia.adapter.auth.in.presentation.dto.request.UserKakaoSignupRequest;

public interface AuthSignupUseCase {
    void signup(UserKakaoSignupRequest userKakaoSignupRequest);
}
