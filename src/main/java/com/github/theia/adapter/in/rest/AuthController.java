package com.github.theia.adapter.in.rest;

import com.github.theia.adapter.in.rest.dto.request.UserLoginRequest;
import com.github.theia.adapter.in.rest.dto.request.UserSignupRequest;
import com.github.theia.adapter.in.rest.dto.respose.TokenResponse;
import com.github.theia.application.port.in.AuthSignupUseCase;
import com.github.theia.application.port.in.KakaoLoginUseCase;
import com.github.theia.domain.user.UserEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.github.theia.global.security.jwt.JwtTokenProvider.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final KakaoLoginUseCase kakaoLoginUseCase;
    private final AuthSignupUseCase authSignupUseCase;

    @PostMapping("/login")
    public ResponseEntity<Void> login(HttpServletResponse httpServletResponse,
                                      @RequestBody UserLoginRequest userLoginRequest) {

        TokenResponse tokenResponse = kakaoLoginUseCase.login(userLoginRequest.getAccessToken());

        httpServletResponse.addHeader(ACCESS_HEADER, BEARER_PREFIX + tokenResponse.getAccessToken());
        httpServletResponse.addHeader(REFRESH_HEADER, BEARER_PREFIX + tokenResponse.getRefreshToken());

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody UserSignupRequest userSignupRequest) {

        authSignupUseCase.signup(userSignupRequest);

        return ResponseEntity.ok().build();
    }
}
