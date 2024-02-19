package com.github.theia.adapter.in.rest;

import com.github.theia.adapter.in.rest.dto.request.UserLoginRequest;
import com.github.theia.adapter.in.rest.dto.respose.TokenResponse;
import com.github.theia.application.port.in.KakaoLoginUseCase;
import com.github.theia.global.security.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.github.theia.global.security.jwt.JwtTokenProvider.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final KakaoLoginUseCase kakaoLoginUseCase;

    @PostMapping("/login")
    public ResponseEntity<Void> login(HttpServletResponse httpServletResponse,
                                      @RequestBody UserLoginRequest userLoginRequest) {

        TokenResponse tokenResponse = kakaoLoginUseCase.login(userLoginRequest.getAccessToken());

        httpServletResponse.addHeader(ACCESS_HEADER, BEARER_PREFIX + tokenResponse.getAccessToken());
        httpServletResponse.addHeader(REFRESH_HEADER, BEARER_PREFIX + tokenResponse.getRefreshToken());

        return ResponseEntity.ok().build();
    }
}
