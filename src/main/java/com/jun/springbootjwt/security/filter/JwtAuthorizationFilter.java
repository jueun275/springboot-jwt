package com.jun.springbootjwt.security.filter;

import com.jun.springbootjwt.security.factory.JwtUtil;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String REPLACEMENT_EMPTY_DELIMITER = "";

    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    public JwtAuthorizationFilter(
            final AuthenticationManager authenticationManager,
            final UserDetailsService userDetailsService,
            final JwtUtil jwtUtil
    ) {
        super(authenticationManager);
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain chain
    ) throws IOException, ServletException {
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (Strings.isEmpty(header)) {
            chain.doFilter(request, response);
            return;
        }

        if (header.startsWith(BEARER_PREFIX)) {
            final String token = header.replace(BEARER_PREFIX, REPLACEMENT_EMPTY_DELIMITER);
            if (jwtUtil.isValidToken(token)) {
                final String email = jwtUtil.extractEmail(token);
                final UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                if (Objects.nonNull(userDetails)) {
                    Authentication authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, //나중에 컨트롤러에서 DI해서 쓸 때 사용하기 편함.
                                    null, // 패스워드는 모르니까 null 처리, 어차피 지금 인증하는게 아니니까!!
                                    userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    // 강제로 시큐리티의 세션에 접근하여 값 저장
                    chain.doFilter(request, response);
                }
            }
        }
    }
}
