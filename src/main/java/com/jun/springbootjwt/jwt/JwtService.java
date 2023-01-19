package com.jun.springbootjwt.jwt;

import com.jun.springbootjwt.endpoint.user.dto.UserDto;
import com.jun.springbootjwt.jwt.refresh.RefreshToken;
import com.jun.springbootjwt.jwt.refresh.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public void save(String email, String refreshToken) {
        RefreshToken token = RefreshToken.builder()
                .email(email)
                .refreshToken(refreshToken)
                .build();
        refreshTokenRepository.save(token);
    }

    public void updateRefreshToken(String email, String updateToken) {
        RefreshToken token = refreshTokenRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        token.update(updateToken);
    }

    public RefreshToken findByEmail(String email) {
        return refreshTokenRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("RefreshToken 값이 존재하지 않습니다."));
    }


}
