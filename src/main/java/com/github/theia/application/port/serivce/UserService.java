package com.github.theia.application.port.serivce;

import com.github.theia.adapter.in.rest.dto.respose.UserInfoResponse;
import com.github.theia.application.port.in.UserInfoUseCase;
import com.github.theia.domain.user.UserEntity;
import com.github.theia.facade.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService implements UserInfoUseCase {

    private final UserFacade userFacade;

    @Override
    @Transactional(readOnly = true)
    public UserInfoResponse findUserInfo() {

        UserEntity user = userFacade.getCurrentUser();

        return UserInfoResponse.builder()
                .userId(user.getUserSeq())
                .userName(user.getUserName())
                .build();
    }
}
