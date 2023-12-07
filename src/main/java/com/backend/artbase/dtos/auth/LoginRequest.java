package com.backend.artbase.dtos.auth;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @NonNull
    private String creds;

    @NonNull
    private String password;
}
