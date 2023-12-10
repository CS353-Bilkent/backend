package com.backend.artbase.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.artbase.entities.ApiResponse;
import com.backend.artbase.entities.User;
import com.backend.artbase.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/me")
    public ResponseEntity<ApiResponse<User>> register(HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        return ResponseEntity.ok(ApiResponse.<User>builder().operationResultData(userService.getUserWithId(user.getUserId())).build());
    }
}
