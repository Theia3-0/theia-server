package com.github.theia.application.port.in;

import com.github.theia.adapter.in.rest.dto.TokenResponse;

public interface KakaoLoginUseCase {
    TokenResponse login(String accessToken);
}
