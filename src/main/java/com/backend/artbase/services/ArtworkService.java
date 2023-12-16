package com.backend.artbase.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.backend.artbase.dtos.artwork.ArtworkSearchResponse;
import com.backend.artbase.entities.ArtworkFilters;
import com.backend.artbase.entities.ArtworkType;
import com.backend.artbase.entities.Material;
import com.backend.artbase.entities.Medium;
import com.backend.artbase.entities.Rarity;
import com.backend.artbase.entities.User;
import com.backend.artbase.entities.UserType;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.backend.artbase.daos.ArtistDao;
import com.backend.artbase.daos.ArtworkDao;
import com.backend.artbase.daos.FileDao;
import com.backend.artbase.dtos.artwork.GetArtworkDisplayDetailsResponse;
import com.backend.artbase.dtos.artwork.UploadArtworkResponse;
import com.backend.artbase.entities.Artist;
import com.backend.artbase.entities.Artwork;
import com.backend.artbase.errors.ArtistRuntimeException;
import com.backend.artbase.errors.ArtworkException;
import com.backend.artbase.errors.UserRuntimeException;

import lombok.RequiredArgsConstructor;

@Transactional
@Service
@RequiredArgsConstructor
public class ArtworkService {

    private final ArtworkDao artworkDao;
    private final ArtistDao artistDao;
    private final FileDao fileDao;
    private final FileService fileService;

    public UploadArtworkResponse saveArtwork(Artwork artwork, MultipartFile image) {

        Integer artworkId = artworkDao.getNextArtworkId();
        artwork.setArtworkId(artworkId);
        artworkDao.saveArtwork(artwork);

        Integer file_id = fileDao.getNextFileId();
        String filename = artworkId.toString() + "_" + file_id.toString() + fileService.checkFileExtension(image.getOriginalFilename());
        fileDao.saveArtworkFile(file_id, filename, artworkId);
        fileService.uploadFile(image, filename);

        return UploadArtworkResponse.builder().artworkId(artworkId).build();
    }

    public Artwork getArtwork(Integer artworkId) {
        Optional<Artwork> artwork = artworkDao.getByArtworkId(artworkId);
        if (artwork.isEmpty()) {
            throw new ArtworkException("Artwork with given ID cannot be found", HttpStatus.NOT_FOUND);
        }

        return artwork.get();
    }

    public List<Artwork> getArtworks(List<Integer> artworkId) {
        List<Artwork> artwork = artworkDao.getByArtworksIds(artworkId);
        if (artwork.isEmpty()) {
            throw new ArtworkException("Artwork with given ID cannot be found", HttpStatus.NOT_FOUND);
        }

        return artwork;
    }

    public GetArtworkDisplayDetailsResponse getArtworkDisplayDetails(Integer artworkId) {
        Artwork artwork = getArtwork(artworkId);

        List<String> filenames = fileDao.getArtworkFilenames(artworkId);

        return GetArtworkDisplayDetailsResponse.builder().artwork(artwork).displayImage(fileService.getFile(filenames.get(0))).build();
    }

    public void addImageToArtwork(MultipartFile image, Integer artworkId) {
        Integer file_id = fileDao.getNextFileId();
        String filename = artworkId.toString() + "_" + file_id.toString();
        fileDao.saveArtworkFile(file_id, filename, artworkId);
        fileService.uploadFile(image, filename);
    }

    public ArtworkSearchResponse searchArtwork(String searchKey) {

        List<Artwork> artworks = artworkDao.searchByName(searchKey);
        if (artworks.isEmpty()) {
            artworks = artworkDao.searchByDescription(searchKey);
        }
        List<String> artistNames = artworkDao.getArtistNamesOfArtworks(artworks);
        return ArtworkSearchResponse.builder().artworks(artworks).artistNames(artistNames).build();
    }

    public ArtworkSearchResponse filterSearchArtwork(String searchKey, ArtworkFilters artworkFilters) {

        if (artworkFilters.getMediumIds().isEmpty() && artworkFilters.getMaterialIds().isEmpty() && artworkFilters.getRarityIds().isEmpty()
                && artworkFilters.getArtworkTypeIds().isEmpty()) {
            return searchArtwork(searchKey);
        }

        // TODO: Uncomment this
        /*
         * if(searchKey.equals(""){ return filterArtwork(artworkFilters); }
         */

        List<Artwork> artworks = artworkDao.filterSearchByName(searchKey, artworkFilters);
        if (artworks.isEmpty()) {
            artworks = artworkDao.filterSearchByDescription(searchKey, artworkFilters);
        }
        List<String> artistNames = artworkDao.getArtistNamesOfArtworks(artworks);
        return ArtworkSearchResponse.builder().artworks(artworks).artistNames(artistNames).build();
    }

    public ArtworkSearchResponse filterArtwork(ArtworkFilters artworkFilters) {

        List<Artwork> filteredArtworks = artworkDao.getArtworkWithFilters(artworkFilters);
        List<String> filteredArtistNames = artworkDao.getArtistNamesOfArtworks(filteredArtworks);
        return ArtworkSearchResponse.builder().artworks(filteredArtworks).artistNames(filteredArtistNames).build();
    }

    public Boolean isArtworkValid(Integer artworkId) {
        return artworkDao.isArtworkValid(artworkId);
    }

    public List<GetArtworkDisplayDetailsResponse> getArtworksOfArtistByUserId(User user) {

        if (!user.getUserType().equals(UserType.ARTIST)) {
            throw new UserRuntimeException("User is not an artist, can not get portfolio information!", HttpStatus.BAD_REQUEST);
        }

        Optional<Artist> optArtist = artistDao.getByUserId(user.getUserId());

        if (optArtist.isEmpty()) {
            throw new ArtistRuntimeException("Artist not found!", HttpStatus.NOT_FOUND);
        }

        Artist artist = optArtist.get();

        List<Artwork> artworkList = artworkDao.getArtworksOfArtist(artist.getArtistId());

        List<GetArtworkDisplayDetailsResponse> responses = new ArrayList<>();
        artworkList.forEach(e -> {

            List<String> filenames = fileDao.getArtworkFilenames(e.getArtworkId());

            responses
                    .add(GetArtworkDisplayDetailsResponse.builder().artwork(e).displayImage(fileService.getFile(filenames.get(0))).build());

        });

        return responses;

    }

    public void updateArtworkDescription(Integer artworkId, String newDescription) {
        Artwork artwork = artworkDao.getByArtworkId(artworkId).orElseThrow(() -> new RuntimeException("Artwork not found"));
        artwork.setArtworkDescription(newDescription);
        artworkDao.updateArtwork(artwork);
    }

    public List<Medium> getMediums() {
        return artworkDao.getMediums();
    }

    public List<ArtworkType> getArtworkTypes() {
        return artworkDao.getArtworkTypes();
    }

    public List<Material> getMaterials() {
        return artworkDao.getMaterials();
    }

    public List<Rarity> getRarities() {
        return artworkDao.getRarities();
    }

}
