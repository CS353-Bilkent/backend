package com.backend.artbase.services;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.artbase.daos.UserDao;
import com.backend.artbase.entities.User;
import com.backend.artbase.entities.UserType;
import com.backend.artbase.errors.UserRuntimeException;

import lombok.RequiredArgsConstructor;

@Transactional
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDao userDao;

    public User getUserWithId(Integer userId) {

        Optional<User> optUser = userDao.getUser(userId);

        if (optUser.isEmpty()) {
            throw new UserRuntimeException("There is no user found with given id: " + userId, HttpStatus.NOT_FOUND);
        }

        return optUser.get();
    }

    public void changeUserType(Integer userId, UserType userType) {
        userDao.changeUserType(userId, userType);
    }

}
