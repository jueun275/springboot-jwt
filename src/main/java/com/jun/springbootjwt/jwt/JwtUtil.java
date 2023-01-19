package com.jun.springbootjwt.jwt;

import com.jun.springbootjwt.jwt.refresh.TokenExpiredTime;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.jun.springbootjwt.jwt.refresh.TokenExpiredTime.ACCESS_TOKEN_EXPIRATION_TIME;
import static com.jun.springbootjwt.jwt.refresh.TokenExpiredTime.REFRESH_TOKEN_EXPIRATION_TIME;

@Slf4j
@Component
public class JwtUtil {
    private final Key key;

    public JwtUtil(@Value(value = "${jwt.secret}") final String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateAccessToken(final String email) {
        return createToken(email, ACCESS_TOKEN_EXPIRATION_TIME.getValue());
    }

    public String generateRefreshToken(final String email) {
        return createToken(email, REFRESH_TOKEN_EXPIRATION_TIME.getValue());
    }

    public boolean isValidToken(final String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (JwtException exception) {
            log.error("JWT Exception");
        }
        return false;
    }

    public String extractEmail(final String token) {
        return extractAllClaims(token).get("email", String.class);
    }

    private String createToken(final String email, final int expiredTime) {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("email", email);

        return Jwts.builder()
                .setHeader(settingHeaders())
                .setClaims(claims)
                .setIssuedAt(settingsDate(0))
                .setExpiration(settingsDate(expiredTime))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    private Date settingsDate(final int plusTime) {
        return Date.from(LocalDateTime.now().plusHours(plusTime)
                .atZone(ZoneId.systemDefault())
                .toInstant()
        );
    }

    private Map<String, Object> settingHeaders() {
        final HashMap<String, Object> headers = new HashMap<>();
        headers.put("type", Header.JWT_TYPE);
        headers.put("algorithm", SignatureAlgorithm.HS512);
        return  headers;
    }

    private <T> T extractClaim(final String token, final Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // 토큰이 유효한 토큰인지 검사한 후, 토큰에 담긴 Payload 값을 가져온다.
    private Claims extractAllClaims(final String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
