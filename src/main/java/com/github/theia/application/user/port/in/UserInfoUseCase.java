package com.github.theia.application.user.port.in;

import com.github.theia.adapter.user.in.persentation.dto.response.UserInfoResponse;

public interface UserInfoUseCase {
    UserInfoResponse findUserInfo();
}
