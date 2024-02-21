package com.github.theia.application.auth.port.in;

import com.github.theia.adapter.auth.in.presentation.dto.respose.LoginUseCaseDto;

public interface KakaoLoginUseCase {
    LoginUseCaseDto login(String accessToken);
}
