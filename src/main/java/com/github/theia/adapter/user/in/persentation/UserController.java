package com.github.theia.adapter.user.in.persentation;

import com.github.theia.adapter.user.in.persentation.dto.response.UserInfoResponse;
import com.github.theia.application.user.port.in.UserInfoUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserInfoUseCase userInfoUseCase;

    @GetMapping
    public ResponseEntity<UserInfoResponse> findUserInfo() {
        return ResponseEntity.ok(userInfoUseCase.findUserInfo());
    }
}
