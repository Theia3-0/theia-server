package com.github.theia.domain.user;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_seq", nullable = false, unique = true)
    private Long userSeq;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_email", nullable = false, unique = true)
    private String userEmail;

    @Column(name = "password")
    private String password;

    @Column(name = "user_profile_url", nullable = false)
    private String userProfileUrl;

    public void editUserName(String userName) {
        this.userName = userName;
    }

    public void editUserProfileUrl(String userProfileUrl) {
        this.userProfileUrl = userProfileUrl;
    }
}
