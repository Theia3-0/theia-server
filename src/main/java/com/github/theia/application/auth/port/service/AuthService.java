package com.github.theia.application.auth.port.service;

import com.github.theia.adapter.auth.in.presentation.dto.request.UserSignupRequest;
import com.github.theia.adapter.auth.in.presentation.dto.respose.KaKaoInfo;
import com.github.theia.adapter.auth.in.presentation.dto.respose.LoginUseCaseDto;
import com.github.theia.adapter.auth.in.presentation.dto.respose.TokenResponse;
import com.github.theia.application.auth.port.in.KakaoLoginUseCase;
import com.github.theia.application.auth.port.in.AuthSignupUseCase;
import com.github.theia.application.auth.port.out.*;
import com.github.theia.domain.refresh.RefreshTokenRedisEntity;
import com.github.theia.domain.user.UserEntity;
import com.github.theia.facade.UserFacade;
import com.github.theia.global.error.exception.TheiaException;
import com.github.theia.global.feign.client.KakaoInformationClient;
import com.github.theia.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.URISyntaxException;

import static com.github.theia.global.error.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class AuthService implements KakaoLoginUseCase, AuthSignupUseCase {

    private final SaveUserPort saveUserPort;
    private final IsUserByEmailPort isUserByEmailPort;
    private final IsUserByNamePort isUserByNamePort;
    private final LoadUserByUserEmailPort loadUserByUserEmailPort;
    private final SaveRefreshTokenPort saveRefreshTokenPort;
    private final UserFacade userFacade;
    private final JwtTokenProvider jwtTokenProvider;
    private final KakaoInformationClient kakaoInformationClient;

    @Value("${spring.kakao.userApiUrl}")
    private String kakaoUserApiUrl;

    @Value("${spring.jwt.refreshExp}")
    public Long refreshExp;

    @Override
    @Transactional
    public LoginUseCaseDto login(String accessToken) {
        try {
            KaKaoInfo kaKaoInfo = kakaoInformationClient.getInfo(new URI(kakaoUserApiUrl), "bearer " + accessToken);

            String userEmail = kaKaoInfo.getKakaoAccount().getEmail();
            boolean isSignedUp = true;

            if (!isUserByEmailPort.isUserByEmail(userEmail)) {
                UserEntity user = UserEntity.builder()
                        .userEmail(userEmail)
                        .build();

                saveUserPort.save(user);

                isSignedUp = false;
            }

            TokenResponse tokenResponse = jwtTokenProvider.getAccessToken(userEmail);

            saveRefreshTokenPort.save(RefreshTokenRedisEntity.builder()
                            .userEmail(userEmail)
                            .refreshToken(tokenResponse.getRefreshToken())
                            .expiredAt(refreshExp)
                    .build());

            return new LoginUseCaseDto(tokenResponse, isSignedUp);

            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public void signup(UserSignupRequest userSignupRequest) {

        UserEntity user = userFacade.getCurrentUser();

        if (isUserByNamePort.isUserByName(userSignupRequest.getUserName()))
            throw new TheiaException(DUPLICATE_USER);

        UserEntity newUser = loadUserByUserEmailPort.findByUserEmail(user.getUserEmail())
                .orElseThrow(() -> new TheiaException(NOT_FOUND_USER));

        newUser.editUserName(userSignupRequest.getUserName());

        saveUserPort.save(newUser);
    }
}
