package com.jun.springbootjwt.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jun.springbootjwt.endpoint.dto.LoginDto;
import com.jun.springbootjwt.security.factory.JwtUtil;
import com.jun.springbootjwt.security.model.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private static final String BEARER_PREFIX = "Bearer ";

    private final ObjectMapper mapper;
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(
            final AuthenticationManager authenticationManager,
            final JwtUtil jwtUtil,
            final ObjectMapper mapper
    ) {
        super(authenticationManager);
        this.jwtUtil = jwtUtil;
        this.mapper = mapper;
    }

    @Override
    public Authentication attemptAuthentication(
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws AuthenticationException {
        try {
            final LoginDto loginUser = mapper.readValue(request.getReader(), LoginDto.class);
            final UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            loginUser.getEmail(),
                            loginUser.getPassword());
            return super.getAuthenticationManager().authenticate(authenticationToken);
        } catch (IOException e) {
            throw new AuthenticationServiceException("Fail to authenticate user");
        }
    }

    @Override
    protected void successfulAuthentication(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain chain,
            final Authentication authResult
    ) throws IOException {
        final String principal = (String) authResult.getPrincipal();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setHeader(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + jwtUtil.generateToken(principal, 1));
        response.getWriter();
    }
}
