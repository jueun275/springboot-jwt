package com.jun.springbootjwt.endpoint.user.dto;

import com.jun.springbootjwt.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class UserDto {
    @NotNull
    private String name;
    @NotNull
    private String email;

    private UserDto(final String name, final String email) {
        this.name = name;
        this.email = email;
    }

    public static UserDto of(final User User) {
        return new UserDto(User.getName(), User.getEmail());
    }
}