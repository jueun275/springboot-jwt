package com.jun.springbootjwt.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jun.springbootjwt.jwt.JwtService;
import com.jun.springbootjwt.jwt.JwtUtil;
import com.jun.springbootjwt.security.model.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class JwtLoginSuccessHandler implements AuthenticationSuccessHandler {

    private static final String BEARER_PREFIX = "Bearer ";
    public static final String AT_HEADER = "ACCESS_TOKEN";
    public static final String RT_HEADER = "REFRESH_TOKEN";

    private final JwtUtil jwtUtil;
    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        log.info("로그인 성공");
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();

        final String email = principal.getEmail();
        String refreshToken = jwtUtil.generateRefreshToken(email);
        String accessToken = jwtUtil.generateAccessToken(email);

        // Refresh Token DB에 저장
        jwtService.save(principal.getEmail(), refreshToken);
        log.info("refresh 토근 발급 및 저장 완료");

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setHeader(AT_HEADER, BEARER_PREFIX + accessToken);
        response.setHeader(RT_HEADER, BEARER_PREFIX + refreshToken);
        response.getWriter();
    }
}
