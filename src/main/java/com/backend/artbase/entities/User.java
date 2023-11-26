package com.backend.artbase.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class User {

    private Integer userId;
    private String userName;
    private String email;
    private String userPassword;
    private UserType userType;
}
