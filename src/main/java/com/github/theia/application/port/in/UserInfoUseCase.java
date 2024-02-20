package com.github.theia.application.port.in;

import com.github.theia.adapter.in.rest.dto.respose.UserInfoResponse;

public interface UserInfoUseCase {
    UserInfoResponse findUserInfo();
}
