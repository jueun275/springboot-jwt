package com.jun.springbootjwt.endpoint.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class SignUpDto {
    @NotNull
    private String name;
    @NotNull
    private String email;
    @NotNull
    private String password;

    @Builder
    public SignUpDto(final String name, final String email, final String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
