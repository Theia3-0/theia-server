package com.github.theia.application.port.serivce;

import com.github.theia.adapter.in.rest.dto.request.UserSignupRequest;
import com.github.theia.adapter.in.rest.dto.respose.KaKaoInfo;
import com.github.theia.adapter.in.rest.dto.respose.TokenResponse;
import com.github.theia.application.port.in.KakaoLoginUseCase;
import com.github.theia.application.port.in.AuthSignupUseCase;
import com.github.theia.application.port.out.*;
import com.github.theia.domain.refresh.RefreshTokenRedisEntity;
import com.github.theia.domain.user.UserEntity;
import com.github.theia.facade.UserFacade;
import com.github.theia.global.error.exception.TheiaException;
import com.github.theia.global.feign.client.KakaoInformationClient;
import com.github.theia.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
    public TokenResponse login(String accessToken) {
        try {
            KaKaoInfo kaKaoInfo = kakaoInformationClient.getInfo(new URI(kakaoUserApiUrl), "bearer " + accessToken);

            String userEmail = kaKaoInfo.getKakaoAccount().getEmail();

            if (!isUserByEmailPort.isUserByEmail(userEmail)) {
                UserEntity user = UserEntity.builder()
                        .userEmail(userEmail)
                        .build();

                saveUserPort.save(user);
            }

            TokenResponse tokenResponse = jwtTokenProvider.getAccessToken(userEmail);

            saveRefreshTokenPort.save(RefreshTokenRedisEntity.builder()
                            .userEmail(userEmail)
                            .refreshToken(tokenResponse.getRefreshToken())
                            .expiredAt(refreshExp)
                    .build());

            return tokenResponse;

        } catch (URISyntaxException e) {
            throw new TheiaException(ERROR_FEIGN);
        }
    }

    @Override
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
