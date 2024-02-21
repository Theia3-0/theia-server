package com.github.theia.adapter.auth.in.presentation;

import com.github.theia.adapter.auth.in.presentation.dto.request.UserLoginRequest;
import com.github.theia.adapter.auth.in.presentation.dto.request.UserSignupRequest;
import com.github.theia.adapter.auth.in.presentation.dto.respose.LoginUseCaseDto;
import com.github.theia.adapter.auth.in.presentation.dto.respose.UserLoginResponse;
import com.github.theia.application.auth.port.in.AuthSignupUseCase;
import com.github.theia.application.auth.port.in.KakaoLoginUseCase;
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
    public ResponseEntity<UserLoginResponse> login(HttpServletResponse httpServletResponse,
                                                   @RequestBody UserLoginRequest userLoginRequest) {

        LoginUseCaseDto loginUseCaseDto = kakaoLoginUseCase.login(userLoginRequest.getAccessToken());

        httpServletResponse.addHeader(ACCESS_HEADER, BEARER_PREFIX + loginUseCaseDto.getTokenResponse().getAccessToken());
        httpServletResponse.addHeader(REFRESH_HEADER, BEARER_PREFIX + loginUseCaseDto.getTokenResponse().getRefreshToken());

        return ResponseEntity.ok(new UserLoginResponse(loginUseCaseDto.isSignedUp()));
    }

    @PatchMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody UserSignupRequest userSignupRequest) {

        authSignupUseCase.signup(userSignupRequest);

        return ResponseEntity.ok().build();
    }
}
