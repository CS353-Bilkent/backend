package com.backend.artbase.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.artbase.daos.UserDao;
import com.backend.artbase.entities.Artist;

import lombok.RequiredArgsConstructor;

@Transactional
@Service
@RequiredArgsConstructor
public class ArtistService {

    private final UserService userService;

    public Artist getArtistByArtistId(Integer artistId) {
        return null;
    }

    public Artist getArtistByUserId(Integer userId) {
        return null;
    }

    public Integer createArtist(Integer userId) {
        return null;
    }

}
