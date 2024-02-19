package com.github.theia.application.port.serivce;

import com.github.theia.adapter.in.rest.dto.KaKaoInfo;
import com.github.theia.adapter.in.rest.dto.TokenResponse;
import com.github.theia.application.port.in.KakaoLoginUseCase;
import com.github.theia.application.port.out.IsUserByEmailPort;
import com.github.theia.application.port.out.SaveUserPort;
import com.github.theia.domain.user.UserEntity;
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
public class AuthService implements KakaoLoginUseCase {

    private final SaveUserPort saveUserPort;
    private final IsUserByEmailPort isUserByEmailPort;
    private final JwtTokenProvider jwtTokenProvider;
    private final KakaoInformationClient kakaoInformationClient;


    @Value("${spring.kakao.userApiUrl}")
    private String kakaoUserApiUrl;

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

            return jwtTokenProvider.getAccessToken(userEmail);

        } catch (URISyntaxException e) {
            throw new TheiaException(ERROR_FEIGN);
        }
    }
}
