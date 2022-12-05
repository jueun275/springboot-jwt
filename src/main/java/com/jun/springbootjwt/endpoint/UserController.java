package com.jun.springbootjwt.endpoint;

import com.jun.springbootjwt.endpoint.dto.SignUpDto;
import com.jun.springbootjwt.endpoint.dto.UserDto;
import com.jun.springbootjwt.endpoint.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/")
public class UserController {

    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<UserDto> join(@RequestBody @Valid SignUpDto requestDto){
        return ResponseEntity.ok(userService.createUser(requestDto));
    }
}
