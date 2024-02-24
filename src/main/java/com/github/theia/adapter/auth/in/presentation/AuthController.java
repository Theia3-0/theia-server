package com.github.theia.adapter.auth.in.presentation;

import com.github.theia.adapter.auth.in.presentation.dto.request.UserEmailLoginRequest;
import com.github.theia.adapter.auth.in.presentation.dto.request.UserEmailSignupRequest;
import com.github.theia.adapter.auth.in.presentation.dto.request.UserKakaoLoginRequest;
import com.github.theia.adapter.auth.in.presentation.dto.request.UserKakaoSignupRequest;
import com.github.theia.adapter.auth.in.presentation.dto.respose.LoginUseCaseDto;
import com.github.theia.adapter.auth.in.presentation.dto.respose.TokenResponse;
import com.github.theia.adapter.auth.in.presentation.dto.respose.UserLoginResponse;
import com.github.theia.application.auth.port.in.AuthLoginUseCase;
import com.github.theia.application.auth.port.in.AuthSignupUseCase;
import com.github.theia.application.auth.port.in.KakaoSignupUseCase;
import com.github.theia.application.auth.port.in.KakaoLoginUseCase;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.github.theia.global.security.jwt.JwtTokenProvider.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final KakaoLoginUseCase kakaoLoginUseCase;
    private final KakaoSignupUseCase kakaoSignupUseCase;
    private final AuthLoginUseCase authLoginUseCase;
    private final AuthSignupUseCase authSignupUseCase;

    @PostMapping("/kakao")
    public ResponseEntity<UserLoginResponse> kakaoLogin(HttpServletResponse httpServletResponse,
                                                   @RequestBody UserKakaoLoginRequest userKakaoLoginRequest) {

        LoginUseCaseDto loginUseCaseDto = kakaoLoginUseCase.login(userKakaoLoginRequest.getAccessToken());

        httpServletResponse.addHeader(ACCESS_HEADER, BEARER_PREFIX + loginUseCaseDto.getTokenResponse().getAccessToken());
        httpServletResponse.addHeader(REFRESH_HEADER, BEARER_PREFIX + loginUseCaseDto.getTokenResponse().getRefreshToken());

        return ResponseEntity.ok(new UserLoginResponse(loginUseCaseDto.isSignedUp()));
    }

    @PatchMapping("/kakao")
    public ResponseEntity<Void> kakaoSignup(@RequestBody UserKakaoSignupRequest userKakaoSignupRequest) {

        kakaoSignupUseCase.signup(userKakaoSignupRequest);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(HttpServletResponse httpServletResponse,
                                      @RequestBody UserEmailLoginRequest userEmailLoginRequest) {

        TokenResponse tokenResponse = authLoginUseCase.login(userEmailLoginRequest);

        httpServletResponse.addHeader(ACCESS_HEADER, BEARER_PREFIX + tokenResponse.getAccessToken());
        httpServletResponse.addHeader(REFRESH_HEADER, BEARER_PREFIX + tokenResponse.getRefreshToken());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestPart(name = "request") UserEmailSignupRequest request,
                                       @RequestPart(name = "profile_img") MultipartFile profileImg) {

        authSignupUseCase.signup(request, profileImg);

        return ResponseEntity.ok().build();
    }
}
