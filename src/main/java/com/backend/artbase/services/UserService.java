package com.backend.artbase.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.artbase.daos.UserDao;
import com.backend.artbase.entities.User;

import lombok.RequiredArgsConstructor;

@Transactional
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDao userDao;

    public User getUserWithId(Integer userId) {
        return userDao.getUser(userId);
    }

}
