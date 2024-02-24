package com.github.theia.adapter.user.in.persentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoResponse {
    private Long userId;
    private String userName;
    private String userProfileUrl;
}
