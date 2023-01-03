package com.jun.springbootjwt.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jun.springbootjwt.security.factory.JwtUtil;
import com.jun.springbootjwt.security.filter.JwtAuthenticationFilter;
import com.jun.springbootjwt.security.filter.JwtAuthorizationFilter;
import com.jun.springbootjwt.security.service.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig  {
//    private final CorsConfig corsConfig;
    private final ObjectMapper mapper;
    private final CustomUserDetailService userDetailsService;
    private final JwtUtil jwtUtil;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationManager authenticationManager = authenticationManager(http.getSharedObject(AuthenticationConfiguration.class));
        http
                .csrf().disable()
                .httpBasic().disable() /*JWT 이외의  session, http basic, loginform 등의 인증방식을 제거 */
                .formLogin().disable()
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .antMatchers("/api/v1/sign-up").permitAll()
                                .antMatchers("/api/v1/login").permitAll()
                                .anyRequest().authenticated()
                )
                .addFilter(new JwtAuthenticationFilter(authenticationManager, jwtUtil, mapper))
                .addFilter(new JwtAuthorizationFilter(authenticationManager, userDetailsService, jwtUtil));
        return http.build();
    }


//    private JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
//        final JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)), jwtUtil, objectMapper);
//        jwtAuthenticationFilter.setFilterProcessesUrl("/api/v1/login");
//        return jwtAuthenticationFilter;
//    }
}
