package com.jun.springbootjwt.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jun.springbootjwt.endpoint.user.dto.LoginDto;
import com.jun.springbootjwt.jwt.JwtService;
import com.jun.springbootjwt.jwt.JwtUtil;
import com.jun.springbootjwt.security.model.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper mapper;

    public JwtAuthenticationFilter(
            final AuthenticationManager authenticationManager,
            final ObjectMapper mapper
    ) {
        super(authenticationManager);
        this.mapper = mapper;
    }

    @Override
    public Authentication attemptAuthentication(
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws AuthenticationException {
        try {
            log.info("JwtAuthenticationFilter : 진입");
            final LoginDto loginUser = mapper.readValue(request.getReader(), LoginDto.class);
            log.info("loginUser : " + loginUser.getEmail());
            final UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            loginUser.getEmail(),
                            loginUser.getPassword());
            log.info("JwtAuthenticationFilter : 토큰생성완료");

            Authentication authentication = super.getAuthenticationManager().authenticate(authenticationToken);
            log.info(" authentication 생성완료" );
            return authentication;

        } catch (IOException e) {
            throw new AuthenticationServiceException("Fail to authenticate user");
        }
    }
}
