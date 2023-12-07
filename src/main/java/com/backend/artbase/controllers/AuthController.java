package com.backend.artbase.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.artbase.dtos.auth.AuthResponse;
import com.backend.artbase.dtos.auth.LoginRequestEmail;
import com.backend.artbase.dtos.auth.LoginRequestUsername;
import com.backend.artbase.dtos.auth.RegisterRequest;
import com.backend.artbase.entities.ApiResponse;
import com.backend.artbase.services.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@RequestBody RegisterRequest registerDto) {
        return ResponseEntity.ok(ApiResponse.<AuthResponse>builder().operationResultData(authService.registerUser(registerDto)).build());
    }

    @PostMapping("/login_email")
    public ResponseEntity<ApiResponse<AuthResponse>> loginWithEmail(@RequestBody LoginRequestEmail loginRequest) {
        return ResponseEntity.ok(ApiResponse.<AuthResponse>builder().operationResultData(authService.loginWithEmail(loginRequest)).build());
    }

    @PostMapping("/login_username")
    public ResponseEntity<ApiResponse<AuthResponse>> loginWithUsername(@RequestBody LoginRequestUsername loginRequest) {
        return ResponseEntity
                .ok(ApiResponse.<AuthResponse>builder().operationResultData(authService.loginWithUsername(loginRequest)).build());
    }

}
