package com.jun.springbootjwt.security.service;

import com.jun.springbootjwt.domain.User;
import com.jun.springbootjwt.domain.UserRepository;
import com.jun.springbootjwt.security.model.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        final User user = userRepository.findUserByEmail(email).orElseThrow(EntityNotFoundException::new);
        return new CustomUserDetails(user, new ArrayList<>());
    }
}
