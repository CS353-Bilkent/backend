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

    public List<ArtworkDto> getArtworks(List<Integer> artworkId) {
        List<ArtworkDto> artwork = artworkDao.getByArtworksIds(artworkId);
        if (artwork.isEmpty()) {
            throw new ArtworkException("Artwork with given ID cannot be found", HttpStatus.NOT_FOUND);
        }

        return artwork;
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

    public List<ArtworkDisplayDetails> getArtworksOfArtistByUserId(User user) {

        if (!user.getUserType().equals(UserType.ARTIST)) {
            throw new UserRuntimeException("User is not an artist, can not get portfolio information!", HttpStatus.BAD_REQUEST);
        }

        Optional<Artist> optArtist = artistDao.getByUserId(user.getUserId());

        if (optArtist.isEmpty()) {
            throw new ArtistRuntimeException("Artist not found!", HttpStatus.NOT_FOUND);
        }

        Artist artist = optArtist.get();

        List<ArtworkDto> artworkDtoList = artworkDao.getArtworksOfArtist(artist.getArtistId());

        List<ArtworkDisplayDetails> responses = new ArrayList<>();
        artworkDtoList.forEach(e -> {

            List<String> filenames = fileDao.getArtworkFilenames(e.getArtworkId());

            responses.add(ArtworkDisplayDetails.builder().artworkDto(e).displayImage(fileService.getFile(filenames.get(0))).build());

        });

        return responses;

    }

    public void updateArtworkDescription(Integer artworkId, String newDescription) {
        ArtworkDto artworkDto = artworkDao.getByArtworkId(artworkId).orElseThrow(() -> new RuntimeException("Artwork not found"));
        artworkDto.setArtworkDescription(newDescription);
        //@formatter:off
        artworkDao.updateArtwork(Artwork.builder()
                .artworkName(artworkDto.getArtworkName())
                .artworkId(artworkDto.getArtworkId())
                .userId(artworkDto.getUserId())
                .artistId(artworkDto.getArtistId())
                .fixedPrice(artworkDto.getFixedPrice())
                .artworkTypeId(artworkDto.getArtworkTypeId())
                .timePeriod(artworkDto.getTimePeriod())
                .rarityId(artworkDto.getRarityId())
                .mediumId(artworkDto.getMediumId())
                .sizeX(artworkDto.getSizeX())
                .sizeY(artworkDto.getSizeY())
                .sizeZ(artworkDto.getSizeZ())
                .materialId(artworkDto.getMaterialId())
                .artworkLocation(artworkDto.getArtworkLocation())
                .artMovementId(artworkDto.getArtMovementId())
                .acquisitionWay(artworkDto.getAcquisitionWay())
                .artworkDescription(artworkDto.getArtworkDescription())
                .artworkStatus(artworkDto.getArtworkStatus())
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

    private ArtworkDisplayDetails getArtworkDisplayDetailsFromDto(ArtworkDto artworkDto) {
        List<String> filenames = fileDao.getArtworkFilenames(artworkDto.getArtworkId());

        return ArtworkDisplayDetails.builder().artworkDto(artworkDto).displayImage(fileService.getFile(filenames.get(0))).build();
    }

    private List<ArtworkDisplayDetails> getArtworksDisplayDetailsFromDtos(List<ArtworkDto> dtoList) {
        List<ArtworkDisplayDetails> displayDetailsList = new ArrayList<>();
        dtoList.forEach(e -> {
            List<String> filenames = fileDao.getArtworkFilenames(e.getArtworkId());

            displayDetailsList
                    .add(ArtworkDisplayDetails.builder().artworkDto(e).displayImage(fileService.getFile(filenames.get(0))).build());
        });

        return displayDetailsList;
    }
}