package com.backend.artbase.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.backend.artbase.dtos.artwork.ArtworkDto;
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
import com.backend.artbase.dtos.artwork.ArtworkDisplayDetails;
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

        Optional<Artist> optArtist = artistDao.getByUserId(artwork.getUserId());

        if (optArtist.isEmpty()) {
            throw new ArtistRuntimeException("Artist not found!", HttpStatus.NOT_FOUND);
        }

        artwork.setArtistId(optArtist.get().getArtistId());
        Integer artworkId = artworkDao.getNextArtworkId();
        artwork.setArtworkId(artworkId);
        artworkDao.saveArtwork(artwork);

        Integer file_id = fileDao.getNextFileId();
        String filename = artworkId.toString() + "_" + file_id.toString() + fileService.checkFileExtension(image.getOriginalFilename());
        fileDao.saveArtworkFile(file_id, filename, artworkId);
        fileService.uploadFile(image, filename);

        return UploadArtworkResponse.builder().artworkId(artworkId).build();
    }

    public ArtworkDto getArtwork(Integer artworkId) {
        Optional<ArtworkDto> artworkDto = artworkDao.getByArtworkId(artworkId);
        if (artworkDto.isEmpty()) {
            throw new ArtworkException("Artwork with given ID cannot be found", HttpStatus.NOT_FOUND);
        }

        return artworkDto.get();
    }

    public ArtworkDisplayDetails getArtworkDisplayDetails(Integer artworkId) {
        ArtworkDto artworkDto = getArtwork(artworkId);
        List<String> filenames = fileDao.getArtworkFilenames(artworkId);

        return ArtworkDisplayDetails.builder().artworkDto(artworkDto).displayImage(fileService.getFile(filenames.get(0))).build();
    }

    public ArtworkSearchResponse searchArtwork(String searchKey) {

        List<ArtworkDto> artworkDtos = artworkDao.searchByName(searchKey);
        if (artworkDtos.isEmpty()) {
            artworkDtos = artworkDao.searchByDescription(searchKey);
        }

        return ArtworkSearchResponse.builder().artworkDtos(getArtworksDisplayDetailsFromDtos(artworkDtos)).build();
    }

    public ArtworkSearchResponse filterSearchArtwork(String searchKey, ArtworkFilters artworkFilters) {

        if (artworkFilters.getMediumIds().isEmpty() && artworkFilters.getMaterialIds().isEmpty() && artworkFilters.getRarityIds().isEmpty()
                && artworkFilters.getArtworkTypeIds().isEmpty() && searchKey.equals("")) {
            return getAllArtworks();
        }

        if (artworkFilters.getMediumIds().isEmpty() && artworkFilters.getMaterialIds().isEmpty() && artworkFilters.getRarityIds().isEmpty()
                && artworkFilters.getArtworkTypeIds().isEmpty()) {
            return searchArtwork(searchKey);
        }

        if (searchKey.equals("")) {
            return filterArtwork(artworkFilters);
        }

        List<ArtworkDto> artworkDtos = artworkDao.filterSearchByName(searchKey, artworkFilters);
        if (artworkDtos.isEmpty()) {
            artworkDtos = artworkDao.filterSearchByDescription(searchKey, artworkFilters);
        }
        return ArtworkSearchResponse.builder().artworkDtos(getArtworksDisplayDetailsFromDtos(artworkDtos)).build();
    }

    public ArtworkSearchResponse filterArtwork(ArtworkFilters artworkFilters) {

        List<ArtworkDto> filteredArtworkDtos = artworkDao.getArtworkWithFilters(artworkFilters);
        return ArtworkSearchResponse.builder().artworkDtos(getArtworksDisplayDetailsFromDtos(filteredArtworkDtos)).build();
    }

    public Boolean isArtworkValid(Integer artworkId) {
        return artworkDao.isArtworkValid(artworkId);
    }

    public ArtworkSearchResponse getArtworksOfArtistByUserId(User user) {

        if (!user.getUserType().equals(UserType.ARTIST)) {
            throw new UserRuntimeException("User is not an artist, can not get portfolio information!", HttpStatus.BAD_REQUEST);
        }

        Optional<Artist> optArtist = artistDao.getByUserId(user.getUserId());

        if (optArtist.isEmpty()) {
            throw new ArtistRuntimeException("Artist not found!", HttpStatus.NOT_FOUND);
        }

        Artist artist = optArtist.get();

        List<ArtworkDto> artworkDtoList = artworkDao.getArtworksOfArtist(artist.getArtistId());

        return ArtworkSearchResponse.builder().artworkDtos(getArtworksDisplayDetailsFromDtos(artworkDtoList)).build();

    }

    public void updateArtworkDescription(Integer artworkId, String newDescription) {
        Artwork artwork = artworkDao.getArtworkByArtworkId(artworkId);
        if (artwork == null) {
            throw new ArtworkException("Artwork not found", HttpStatus.NOT_FOUND);
        }
        artwork.setArtworkDescription(newDescription);

        //@formatter:off
        artworkDao.updateArtwork(Artwork.builder()
                .artworkName(artwork.getArtworkName())
                .artworkId(artwork.getArtworkId())
                .userId(artwork.getUserId())
                .artistId(artwork.getArtistId())
                .fixedPrice(artwork.getFixedPrice())
                .artworkTypeId(artwork.getArtworkTypeId())
                .timePeriod(artwork.getTimePeriod())
                .rarityId(artwork.getRarityId())
                .mediumId(artwork.getMediumId())
                .sizeX(artwork.getSizeX())
                .sizeY(artwork.getSizeY())
                .sizeZ(artwork.getSizeZ())
                .materialId(artwork.getMaterialId())
                .artworkLocation(artwork.getArtworkLocation())
                .artMovementId(artwork.getArtMovementId())
                .acquisitionWay(artwork.getAcquisitionWay())
                .artworkDescription(artwork.getArtworkDescription())
                .artworkStatus(artwork.getArtworkStatus())
                .build());
        //@formatter:on
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

    public ArtworkSearchResponse getAllArtworks() {
        List<ArtworkDto> artworkDtos = artworkDao.getAllArtworks();
        return ArtworkSearchResponse.builder().artworkDtos(getArtworksDisplayDetailsFromDtos(artworkDtos)).build();
    }

    public List<ArtworkDisplayDetails> getArtworksDisplayDetailsFromDtos(List<ArtworkDto> dtoList) {
        List<ArtworkDisplayDetails> displayDetailsList = new ArrayList<>();
        dtoList.forEach(e -> {
            List<String> filenames = fileDao.getArtworkFilenames(e.getArtworkId());

            displayDetailsList
                    .add(ArtworkDisplayDetails.builder().artworkDto(e).displayImage(fileService.getFile(filenames.get(0))).build());
        });

        return displayDetailsList;
    }
}