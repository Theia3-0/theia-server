package com.github.theia.adapter.auth.in.presentation.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserEmailLoginRequest {
    private String email;
    private String password;
}
