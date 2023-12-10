package com.backend.artbase.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.backend.artbase.daos.ArtworkDao;
import com.backend.artbase.entities.Artwork;

import lombok.RequiredArgsConstructor;

@Transactional
@Service
@RequiredArgsConstructor
public class ArtworkService {

    private final ArtworkDao artworkDao;
    private final FileService fileService;

    public void saveArtwork(Artwork artwork, MultipartFile[] images) throws Exception {

        Integer artworkId = artworkDao.getNextArtworkId();
        artwork.setArtworkId(artworkId);
        artworkDao.saveArtwork(artwork);

        String filename = artworkId + "_" + artwork.getArtworkName();
        fileService.uploadFiles(images, filename);

    }

}
