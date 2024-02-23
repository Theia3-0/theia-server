package com.github.theia.adapter.auth.in.presentation.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserEmailSignupRequest {
    private String user_email;
    private String password;
    private String user_name;
}
