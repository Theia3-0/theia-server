package com.github.theia.application.auth.port.in;

import com.github.theia.adapter.auth.in.presentation.dto.respose.TokenResponse;

public interface KakaoLoginUseCase {
    TokenResponse login(String accessToken);
}
