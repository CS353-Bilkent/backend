package com.backend.artbase.dtos.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthResponse {

    private String jwtToken;
    private Integer user_id;
}
