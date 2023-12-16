package com.backend.artbase.services;

import java.util.List;
import java.util.Optional;

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
        List<byte[]> imageList = fileService.getArtworkFiles(artworkId);

        return GetArtworkDisplayDetailsResponse.builder().artwork(artwork).displayImage(imageList.get(0)).build();
    }

    public void addImageToArtwork(MultipartFile image, Integer artworkId) {
        Integer file_id = fileDao.getNextFileId();
        String filename = artworkId.toString() + "_" + file_id.toString();
        fileDao.saveArtworkFile(file_id, filename, artworkId);
        fileService.uploadFile(image, filename);
    }


    public ArtworkSearchResponse searchArtwork(String searchKey){

        List<Artwork> artworks = artworkDao.searchByName(searchKey);
        if(artworks.isEmpty()){
            artworks = artworkDao.searchByDescription(searchKey);
        }
        List<String> artistNames = artworkDao.getArtistNamesOfArtworks(artworks);
        return ArtworkSearchResponse.builder().artworks(artworks).artistNames(artistNames).build();
    }

    public ArtworkSearchResponse filterSearchArtwork(String searchKey, ArtworkFilters artworkFilters){

        if(artworkFilters.getMediumIds().isEmpty() && artworkFilters.getMaterialIds().isEmpty()
        && artworkFilters.getRarityIds().isEmpty() && artworkFilters.getArtworkTypeIds().isEmpty()){
            return searchArtwork(searchKey);
        }

        //TODO: Uncomment this
        /*
        if(searchKey.equals(""){
            return filterArtwork(artworkFilters);
        }
         */

        List<Artwork> artworks = artworkDao.filterSearchByName(searchKey, artworkFilters);
        if(artworks.isEmpty()){
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

    public List<Artwork> getArtworksOfArtist(Integer artistId) {
        return artworkDao.getArtworksOfArtist(artistId);
    }

    public void updateArtworkDescription(Integer artworkId, String newDescription) {
        Artwork artwork = artworkDao.getByArtworkId(artworkId)
                .orElseThrow(() -> new RuntimeException("Artwork not found"));
        artwork.setArtworkDescription(newDescription);
        artworkDao.updateArtwork(artwork);
    }
}
