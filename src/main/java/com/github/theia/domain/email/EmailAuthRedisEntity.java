package com.github.theia.domain.email;

import jakarta.persistence.Column;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash("emailAuth")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class EmailAuthRedisEntity {
    @Id
    @Column(name = "email")
    private String email;

    @Column(name = "code")
    private String code;

    @Column(name = "authentication")
    private Boolean authentication;

    @TimeToLive
    @Column(name = "expired_at")
    private Long expiredAt;

    public void updateCode(String code) {
        this.code = code;
    }
}
