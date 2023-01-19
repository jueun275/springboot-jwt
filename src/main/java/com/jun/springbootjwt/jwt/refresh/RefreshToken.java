package com.jun.springbootjwt.jwt.refresh;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.*;

@Getter
@RedisHash("refreshToken")
@NoArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String email;
    private String refreshToken;


    public void update(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Builder
    public RefreshToken(Long id, String email, String refreshToken) {
        this.id = id;
        this.email = email;
        this.refreshToken = refreshToken;
    }
}
