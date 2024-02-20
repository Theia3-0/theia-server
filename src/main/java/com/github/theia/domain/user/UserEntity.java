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

    public void editUserName(String userName) {
        this.userName = userName;
    }
}
