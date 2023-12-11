package com.backend.artbase.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.backend.artbase.daos.ArtworkDao;
import com.backend.artbase.dtos.artwork.GetArtworkDisplayDetailsResponse;
import com.backend.artbase.dtos.artwork.GetArtworkFullDetailsResponse;
import com.backend.artbase.entities.Artwork;
import com.backend.artbase.errors.ArtworkException;

import lombok.RequiredArgsConstructor;

@Transactional
@Service
@RequiredArgsConstructor
public class ArtworkService {

    private final ArtworkDao artworkDao;
    private final FileService fileService;

    public void saveArtwork(Artwork artwork, MultipartFile image) {

        Integer artworkId = artworkDao.getNextArtworkId();
        artwork.setArtworkId(artworkId);
        artworkDao.saveArtwork(artwork);

        String filename = artworkId.toString();
        fileService.uploadArtworkFiles(List.of(image).toArray(new MultipartFile[1]), filename, artworkId);

    }

    public Artwork getArtwork(Integer artworkId) {
        Optional<Artwork> artwork = artworkDao.getByArtworkId(artworkId);
        if (artwork.isEmpty()) {
            throw new ArtworkException("Artwork with given ID cannot be found", HttpStatus.NOT_FOUND);
        }

        return artwork.get();
    }

    public GetArtworkDisplayDetailsResponse getArtworkDisplayDetails(Integer artworkId) {
        Artwork artwork = getArtwork(artworkId);
        List<byte[]> imageList = fileService.getArtworkFiles(artworkId);

        return GetArtworkDisplayDetailsResponse.builder().artwork(artwork).displayImage(imageList.get(0)).build();
    }

    public GetArtworkFullDetailsResponse getArtworkFullDetails(Integer artworkId) {
        Artwork artwork = getArtwork(artworkId);
        List<byte[]> imageList = fileService.getArtworkFiles(artworkId);

        return GetArtworkFullDetailsResponse.builder().artwork(artwork).displayImages(imageList).build();
    }

}
