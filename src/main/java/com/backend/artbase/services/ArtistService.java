package com.backend.artbase.services;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.artbase.daos.ArtistDao;
import com.backend.artbase.daos.UserDao;
import com.backend.artbase.dtos.artist.CreateArtistRequest;
import com.backend.artbase.entities.Artist;
import com.backend.artbase.entities.User;
import com.backend.artbase.entities.UserType;
import com.backend.artbase.errors.ArtistRuntimeException;

import lombok.RequiredArgsConstructor;

@Transactional
@Service
@RequiredArgsConstructor
public class ArtistService {

    private final UserService userService;
    private final ArtistDao artistDao;

    public Artist getArtistByArtistId(Integer artistId) {
        Optional<Artist> opARtist = artistDao.getByArtistId(artistId);

        if (opARtist.isEmpty()) {
            throw new ArtistRuntimeException("Artist not found for id " + artistId, HttpStatus.NOT_FOUND);
        }

        return opARtist.get();
    }

    public Artist getArtistByUserId(Integer userId) {
        Optional<Artist> opARtist = artistDao.getByUserId(userId);

        if (opARtist.isEmpty()) {
            throw new ArtistRuntimeException("Artist not found for user id " + userId, HttpStatus.NOT_FOUND);
        }

        return opARtist.get();
    }

    public Integer createArtist(CreateArtistRequest request, User user) {

        Optional<Artist> optArtist = artistDao.getByUserId(user.getUserId());

        if (optArtist.isPresent()) {
            throw new ArtistRuntimeException("Artist already registered", HttpStatus.NOT_ACCEPTABLE);
        }
        Integer artistId = artistDao.getNextArtistId();

        //@formatter:off
        Artist artist = 
            Artist.builder()
                .age(request.getAge())
                .artistId(artistId)
                .userId(user.getUserId())
                .artistName(request.getArtistName())
                .gender(request.getGender())
                .nationality(request.getNationality())
                .speciality(request.getSpeciality())
            .build(); 
        //@formatter:on

        artistDao.saveArtist(artist);

        userService.changeUserType(user.getUserId(), UserType.ARTIST);

        return artistId;
    }

}
