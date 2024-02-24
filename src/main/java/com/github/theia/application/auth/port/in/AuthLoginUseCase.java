package com.github.theia.application.auth.port.in;

import com.github.theia.adapter.auth.in.presentation.dto.request.UserEmailLoginRequest;
import com.github.theia.adapter.auth.in.presentation.dto.respose.TokenResponse;

public interface AuthLoginUseCase {
    TokenResponse login(UserEmailLoginRequest userEmailLoginRequest);
}
