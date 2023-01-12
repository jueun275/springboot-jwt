package com.jun.springbootjwt.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class User extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String name;

    @Enumerated(EnumType.STRING)
    @Column
    private Role role;

    @Builder
    public User(String email, String password, String name, Role role){
        this.email =email;
        this.name = name;
        this.password = password;
        this.role = role;
    }

    public String getRoleKey(){
        return  this.role.getKey();
    }
}
