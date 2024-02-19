package com.github.theia.adapter.in.rest;

import com.github.theia.adapter.in.rest.dto.request.UserLoginRequest;
import com.github.theia.adapter.in.rest.dto.respose.TokenResponse;
import com.github.theia.application.port.in.KakaoLoginUseCase;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final KakaoLoginUseCase kakaoLoginUseCase;

    @PostMapping("/login")
    public ResponseEntity<Void> login(HttpServletResponse httpServletResponse,
                                      @RequestBody UserLoginRequest userLoginRequest) {

        TokenResponse tokenResponse = kakaoLoginUseCase.login(userLoginRequest.getAccessToken());

        httpServletResponse.addHeader("Authorization", "Bearer " + tokenResponse.getAccessToken());
        httpServletResponse.addHeader("Refresh-Token", "Bearer " + tokenResponse.getRefreshToken());

        return ResponseEntity.ok().build();
    }
}
