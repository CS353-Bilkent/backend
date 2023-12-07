package com.backend.artbase.dtos.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestUsername {

    private String username;
    private String password;
}
