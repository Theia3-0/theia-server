package com.github.theia.domain.email;

import jakarta.persistence.Column;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash(value = "emailAuth")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class EmailAuthRedisEntity {
    @Id
    @Indexed
    @Column(name = "email")
    private String email;

    @Column(name = "code")
    private String code;

    @Column(name = "authentication")
    private Boolean authentication;

    @Column(name = "attempt_count")
    private Integer attemptCount;

    @TimeToLive
    @Column(name = "expired_at")
    private Long expiredAt;

    public void updateCode(String code) {
        this.code = code;
        this.attemptCount++;
    }

    public void updateAuthentication(Boolean authentication) {
        this.authentication = authentication;
    }
}
