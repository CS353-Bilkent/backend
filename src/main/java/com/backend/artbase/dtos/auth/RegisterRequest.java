package com.backend.artbase.dtos.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    private String userName;
    private String email;
    private String userPassword;
}
