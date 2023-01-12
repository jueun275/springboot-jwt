package com.jun.springbootjwt.endpoint.user.service;

import com.jun.springbootjwt.domain.Role;
import com.jun.springbootjwt.domain.User;
import com.jun.springbootjwt.domain.UserRepository;
import com.jun.springbootjwt.endpoint.user.dto.LoginDto;
import com.jun.springbootjwt.endpoint.user.dto.SignUpDto;
import com.jun.springbootjwt.endpoint.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.Optional;

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
                .role(Role.USER)
                .build();

        return UserDto.of(userRepository.save(user));
    }

//    @Transactional
//    public UserDto longinUser(@Valid final LoginDto loginDto){
//        User user = Optional.ofNullable(userRepository.findByUserIdAndUserPassword(loginDto.getEmail(), loginDto.getPassword()))
//                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. email = " + loginDto.getEmail()));
//        return UserDto.of(user);
//    }
//

//    private void validateDuplicate(final String email) {
//        final User existUser = userRepository.findUserByEmail(email).orElse(null);
//        if (existUser != null) {
//            throw new IllegalArgumentException("duplicated id");
//        }
//    }

}
