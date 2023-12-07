package com.backend.artbase.services;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.artbase.daos.UserDao;
import com.backend.artbase.dtos.auth.AuthResponse;
import com.backend.artbase.dtos.auth.LoginRequestEmail;
import com.backend.artbase.dtos.auth.LoginRequestUsername;
import com.backend.artbase.dtos.auth.RegisterRequest;
import com.backend.artbase.entities.User;
import com.backend.artbase.entities.UserType;
import com.backend.artbase.errors.UserRuntimeException;
import com.backend.artbase.utils.JwtTokenUtil;

import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import lombok.RequiredArgsConstructor;

@Transactional
@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtTokenUtil jwtTokenUtil;
    private final UserDao userDao;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse registerUser(RegisterRequest registerDto) {

        isValidUsernameFormat(registerDto.getUserName());
        isValidEmail(registerDto.getEmail());

        String encodedPassword = passwordEncoder.encode(registerDto.getUserPassword());
        User user = new User();
        user.setUserName(registerDto.getUserName());
        user.setEmail(registerDto.getEmail());
        user.setUserPassword(encodedPassword);
        user.setUserType(UserType.NORMAL);

        userDao.saveUser(user);

        mailService.sendRegisterMail(user.getEmail());

        String jwt = jwtTokenUtil.generateAccessToken(userDao.getUserByUsername(user.getUserName()));

        return AuthResponse.builder().jwtToken(jwt).user_id(user.getUserId()).build();
    }

    public AuthResponse loginWithUsername(LoginRequestUsername loginRequest) {
        isValidUsernameFormat(loginRequest.getUsername());

        User user = userDao.getUserByUsername(loginRequest.getUsername());

        if (user == null) {
            throw new UserRuntimeException("User not found", HttpStatus.NOT_FOUND);
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getUserPassword())) {
            throw new UserRuntimeException("Password did not match", HttpStatus.NOT_ACCEPTABLE);
        }

        String jwt = jwtTokenUtil.generateAccessToken(userDao.getUserByUsername(user.getUserName()));

        return AuthResponse.builder().jwtToken(jwt).user_id(user.getUserId()).build();
    }

    public AuthResponse loginWithEmail(LoginRequestEmail loginRequest) {

        isValidEmail(loginRequest.getEmail());
        User user = userDao.getUserByEmail(loginRequest.getEmail());

        if (user == null) {
            throw new UserRuntimeException("User not found", HttpStatus.NOT_FOUND);
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getUserPassword())) {
            throw new UserRuntimeException("Password did not match", HttpStatus.NOT_ACCEPTABLE);
        }

        String jwt = jwtTokenUtil.generateAccessToken(userDao.getUserByUsername(user.getUserName()));

        return AuthResponse.builder().jwtToken(jwt).user_id(user.getUserId()).build();
    }

    private boolean isValidUsernameFormat(String username) {
        if (username != null && username.matches("^[a-zA-Z0-9]+$")) {
            return true;
        } else {
            throw new UserRuntimeException("Username does not suit to given format", HttpStatus.BAD_REQUEST);
        }
    }

    public boolean isValidEmail(String email) {
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            throw new UserRuntimeException("Email is not valid", HttpStatus.NOT_ACCEPTABLE);
        }

        return true;
    }
}
