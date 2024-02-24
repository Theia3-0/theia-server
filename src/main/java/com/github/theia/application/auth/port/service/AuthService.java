package com.github.theia.application.auth.port.service;

import com.github.theia.adapter.auth.in.presentation.dto.request.UserEmailLoginRequest;
import com.github.theia.adapter.auth.in.presentation.dto.request.UserEmailSignupRequest;
import com.github.theia.adapter.auth.in.presentation.dto.request.UserKakaoSignupRequest;
import com.github.theia.adapter.auth.in.presentation.dto.respose.KaKaoInfo;
import com.github.theia.adapter.auth.in.presentation.dto.respose.LoginUseCaseDto;
import com.github.theia.adapter.auth.in.presentation.dto.respose.TokenResponse;
import com.github.theia.application.auth.port.in.AuthLoginUseCase;
import com.github.theia.application.auth.port.in.AuthSignupUseCase;
import com.github.theia.application.auth.port.in.KakaoLoginUseCase;
import com.github.theia.application.auth.port.in.KakaoSignupUseCase;
import com.github.theia.application.auth.port.out.*;
import com.github.theia.application.email.port.out.LoadEmailAuthByEmailPort;
import com.github.theia.domain.email.EmailAuthRedisEntity;
import com.github.theia.domain.refresh.RefreshTokenRedisEntity;
import com.github.theia.domain.user.UserEntity;
import com.github.theia.facade.UserFacade;
import com.github.theia.global.error.exception.TheiaException;
import com.github.theia.global.feign.client.KakaoInformationClient;
import com.github.theia.global.security.jwt.JwtTokenProvider;
import com.github.theia.s3.S3Manager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;

import static com.github.theia.global.error.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class AuthService implements KakaoLoginUseCase, KakaoSignupUseCase, AuthSignupUseCase, AuthLoginUseCase {

    private final SaveUserPort saveUserPort;
    private final IsUserByNamePort isUserByNamePort;
    private final LoadUserByUserEmailPort loadUserByUserEmailPort;
    private final SaveRefreshTokenPort saveRefreshTokenPort;
    private final UserFacade userFacade;
    private final JwtTokenProvider jwtTokenProvider;
    private final KakaoInformationClient kakaoInformationClient;
    private final LoadEmailAuthByEmailPort loadEmailAuthByEmailPort;
    private final PasswordEncoder passwordEncoder;
    private final S3Manager s3Manager;

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

            UserEntity currentUser = loadUserByUserEmailPort.findByUserEmail(userEmail).orElse(null);

            if (currentUser == null) {
                UserEntity newUser = UserEntity.builder()
                        .userEmail(userEmail)
                        .build();

                saveUserPort.save(newUser);

                isSignedUp = false;
            } else if (currentUser.getUserName().isEmpty()) {
                isSignedUp = false;
            }

            TokenResponse tokenResponse = jwtTokenProvider.getToken(userEmail);

            saveRefreshTokenPort.save(RefreshTokenRedisEntity.builder()
                            .userEmail(userEmail)
                            .refreshToken(tokenResponse.getRefreshToken())
                            .expiredAt(refreshExp)
                    .build());

            return new LoginUseCaseDto(tokenResponse, isSignedUp);

            } catch (Exception e) {
                throw new TheiaException(ERROR_FEIGN);
        }
    }

    @Override
    @Transactional
    public void signup(UserKakaoSignupRequest userKakaoSignupRequest, MultipartFile profileImg) {

        UserEntity user = userFacade.getCurrentUser();
        String user_profile_url;

        if (isUserByNamePort.isUserByName(userKakaoSignupRequest.getUserName()))
            throw new TheiaException(DUPLICATE_USER);

        UserEntity newUser = loadUserByUserEmailPort.findByUserEmail(user.getUserEmail())
                .orElseThrow(() -> new TheiaException(NOT_FOUND_USER));

        try {
            user_profile_url = s3Manager.uploadImages(profileImg);
        } catch (IOException e) {
            throw new TheiaException(ERROR_S3);
        }

        newUser.editUserName(userKakaoSignupRequest.getUserName());
        newUser.editUserProfileUrl(user_profile_url);

        saveUserPort.save(newUser);
    }

    @Override
    public TokenResponse login(UserEmailLoginRequest userEmailLoginRequest) {
        UserEntity user = loadUserByUserEmailPort.findByUserEmail(userEmailLoginRequest.getEmail())
                .orElseThrow(() -> new TheiaException(NOT_FOUND_USER));

        String email = user.getUserEmail();
        String password = user.getPassword();

        if (!passwordEncoder.matches(userEmailLoginRequest.getPassword(), password))
            throw new TheiaException(NOT_MATCH_PASSWORD);

        return jwtTokenProvider.getToken(email);
    }

    @Override
    @Transactional
    public void signup(UserEmailSignupRequest userEmailSignupRequest, MultipartFile profileImg) {
        String email = userEmailSignupRequest.getEmail();
        String password = passwordEncoder.encode(userEmailSignupRequest.getPassword());
        String user_name = userEmailSignupRequest.getUserName();
        String user_profile_url;

        EmailAuthRedisEntity emailAuth = loadEmailAuthByEmailPort.findByEmail(email)
                .orElseThrow(() -> new TheiaException(NOT_FOUND_EMAIL));

        if (!emailAuth.getAuthentication())
            throw new TheiaException(UNAUTHORIZATION_EMAIL);

        try {
            user_profile_url = s3Manager.uploadImages(profileImg);
        } catch (IOException e) {
            throw new TheiaException(ERROR_S3);
        }

        UserEntity newUser = UserEntity.builder()
                    .userEmail(email)
                    .userName(user_name)
                    .password(password)
                    .userProfileUrl(user_profile_url)
                .build();

        saveUserPort.save(newUser);
    }
}
