package com.backend.artbase.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.artbase.daos.UserDao;
import com.backend.artbase.utils.JwtTokenUtil;

import lombok.RequiredArgsConstructor;

@Transactional
@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtTokenUtil jwtTokenUtil;
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

}
