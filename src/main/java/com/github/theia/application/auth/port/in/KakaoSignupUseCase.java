package com.github.theia.application.auth.port.in;

import com.github.theia.adapter.auth.in.presentation.dto.request.UserKakaoSignupRequest;
import org.springframework.web.multipart.MultipartFile;

public interface KakaoSignupUseCase {
    void signup(UserKakaoSignupRequest userKakaoSignupRequest, MultipartFile profileImg);
}
