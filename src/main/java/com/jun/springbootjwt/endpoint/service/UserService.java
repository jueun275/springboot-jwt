package com.jun.springbootjwt.endpoint.service;

import com.jun.springbootjwt.domain.User;
import com.jun.springbootjwt.domain.UserRepository;
import com.jun.springbootjwt.endpoint.dto.SignUpDto;
import com.jun.springbootjwt.endpoint.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserDto createUser(@Valid final SignUpDto signUpDto) {
        final User existUser = userRepository.findUserByEmail(signUpDto.getEmail()).orElse(null);
        if (existUser != null) {
            throw new IllegalArgumentException("duplicated id");
        }
        final User user = User.builder()
                .name(signUpDto.getName())
                .email(signUpDto.getEmail())
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .build();

        return UserDto.of(userRepository.save(user));
    }

/*    private void validateDuplicate(final String email) {
        final User existUser = userRepository.findUserByEmail(email).orElse(null);
        if (existUser != null) {
            throw new IllegalArgumentException("duplicated id");
        }
    }*/

}
