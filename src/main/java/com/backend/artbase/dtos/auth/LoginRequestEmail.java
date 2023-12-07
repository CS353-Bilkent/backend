package com.backend.artbase.dtos.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestEmail {
    private String email;
    private String password;
}
