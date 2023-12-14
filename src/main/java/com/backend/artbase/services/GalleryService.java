package com.backend.artbase.services;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.artbase.daos.GalleryDao;
import com.backend.artbase.dtos.gallery.CreateGalleryRequest;
import com.backend.artbase.entities.ArtGallery;
import com.backend.artbase.entities.Artist;
import com.backend.artbase.entities.User;
import com.backend.artbase.entities.UserType;
import com.backend.artbase.errors.ArtistRuntimeException;

import lombok.RequiredArgsConstructor;

@Transactional
@Service
@RequiredArgsConstructor
public class GalleryService {

    private final GalleryDao galleryDao;
    private final UserService userService;

    public Integer createGallery(CreateGalleryRequest request, User user) {
        Optional<ArtGallery> optArtist = galleryDao.getByUserId(user.getUserId());

        if (optArtist.isPresent()) {
            throw new ArtistRuntimeException("Artist already registered", HttpStatus.NOT_ACCEPTABLE);
        }
        Integer galleryId = galleryDao.getNextGalleryId();

        //@formatter:off
                ArtGallery gallery = 
                    ArtGallery.builder()
                        .artGalleryId(galleryId)
                        .artGalleryLocation(request.getArtGalleryLocation())
                        .artGalleryName(request.getArtGalleryName())
                        .userId(user.getUserId())
                    .build(); 
        //@formatter:on

        galleryDao.saveGallery(gallery);

        userService.changeUserType(galleryId, UserType.GALLERY);

        return galleryId;
    }

}
