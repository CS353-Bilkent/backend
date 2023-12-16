package com.backend.artbase.services;

import java.util.List;
import java.util.Optional;

import com.backend.artbase.dtos.artwork.ArtworkDto;
import com.backend.artbase.dtos.artwork.ArtworkSearchResponse;
import com.backend.artbase.entities.ArtworkFilters;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.backend.artbase.daos.ArtworkDao;
import com.backend.artbase.daos.FileDao;
import com.backend.artbase.dtos.artwork.GetArtworkDisplayDetailsResponse;
import com.backend.artbase.dtos.artwork.UploadArtworkResponse;
import com.backend.artbase.entities.Artwork;
import com.backend.artbase.errors.ArtworkException;

import lombok.RequiredArgsConstructor;

@Transactional
@Service
@RequiredArgsConstructor
public class ArtworkService {

    private final ArtworkDao artworkDao;
    private final FileDao fileDao;
    private final FileService fileService;

    public UploadArtworkResponse saveArtwork(Artwork artwork, MultipartFile image) {

        Integer artworkId = artworkDao.getNextArtworkId();
        artwork.setArtworkId(artworkId);
        artworkDao.saveArtwork(artwork);

        Integer file_id = fileDao.getNextFileId();
        String filename = artworkId.toString() + "_" + file_id.toString();
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

    public GetArtworkDisplayDetailsResponse getArtworkDisplayDetails(Integer artworkId) {
        ArtworkDto artworkDto = getArtwork(artworkId);
        List<byte[]> imageList = fileService.getArtworkFiles(artworkId);

        return GetArtworkDisplayDetailsResponse.builder().artworkDto(artworkDto).displayImage(imageList.get(0)).build();
    }

    public void addImageToArtwork(MultipartFile image, Integer artworkId) {
        Integer file_id = fileDao.getNextFileId();
        String filename = artworkId.toString() + "_" + file_id.toString();
        fileDao.saveArtworkFile(file_id, filename, artworkId);
        fileService.uploadFile(image, filename);
    }


    public ArtworkSearchResponse searchArtwork(String searchKey){

        List<ArtworkDto> artworkDtos = artworkDao.searchByName(searchKey);
        if(artworkDtos.isEmpty()){
            artworkDtos = artworkDao.searchByDescription(searchKey);
        }
        return ArtworkSearchResponse.builder().artworkDtos(artworkDtos).build();
    }

    public ArtworkSearchResponse filterSearchArtwork(String searchKey, ArtworkFilters artworkFilters){

        if(artworkFilters.getMediumIds().isEmpty() && artworkFilters.getMaterialIds().isEmpty()
                && artworkFilters.getRarityIds().isEmpty() && artworkFilters.getArtworkTypeIds().isEmpty() && searchKey.equals("")){
            return getAllArtworks();
        }

        if(artworkFilters.getMediumIds().isEmpty() && artworkFilters.getMaterialIds().isEmpty()
        && artworkFilters.getRarityIds().isEmpty() && artworkFilters.getArtworkTypeIds().isEmpty()){
            return searchArtwork(searchKey);
        }

        if(searchKey.equals("")){
            return filterArtwork(artworkFilters);
        }

        List<ArtworkDto> artworkDtos = artworkDao.filterSearchByName(searchKey, artworkFilters);
        if(artworkDtos.isEmpty()){
            artworkDtos = artworkDao.filterSearchByDescription(searchKey, artworkFilters);
        }
        return ArtworkSearchResponse.builder().artworkDtos(artworkDtos).build();
    }
  
    public ArtworkSearchResponse filterArtwork(ArtworkFilters artworkFilters) {

        List<ArtworkDto> filteredArtworkDtos = artworkDao.getArtworkWithFilters(artworkFilters);
        return ArtworkSearchResponse.builder().artworkDtos(filteredArtworkDtos).build();
    }

    public Boolean isArtworkValid(Integer artworkId) {
        return artworkDao.isArtworkValid(artworkId);
    }

    public ArtworkSearchResponse getAllArtworks(){
        List<ArtworkDto> artworkDtos = artworkDao.getAllArtworks();
        return ArtworkSearchResponse.builder().artworkDtos(artworkDtos).build();
    }
}
