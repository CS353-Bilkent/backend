package com.backend.artbase.controllers;

import com.backend.artbase.dtos.artwork.ArtworkSearchResponse;
import com.backend.artbase.dtos.artwork.DescriptionDto;
import com.backend.artbase.entities.ApiResponse;
import com.backend.artbase.entities.Artwork;
import com.backend.artbase.entities.ArtworkFilters;
import com.backend.artbase.entities.ArtworkStatus;
import com.backend.artbase.entities.ArtworkType;
import com.backend.artbase.entities.Material;
import com.backend.artbase.entities.Medium;
import com.backend.artbase.entities.Rarity;
import com.backend.artbase.entities.User;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.backend.artbase.dtos.artwork.ArtworkDisplayDetails;
import com.backend.artbase.dtos.artwork.GetFilteredArtworksRequest;
import com.backend.artbase.dtos.artwork.UploadArtworkResponse;
import com.backend.artbase.errors.ArtworkException;
import com.backend.artbase.services.ArtworkService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequiredArgsConstructor
@RequestMapping("/art")
public class ArtworkController {

    private final ArtworkService artworkService;

    //@formatter:off
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<UploadArtworkResponse>> saveArtwork(
            @RequestPart(name = "image") MultipartFile image,
            @RequestPart(name = "name") String artworkName,
            @RequestPart(name = "fixedPrice", required = false) Double fixedPrice,
            @RequestPart(name = "artworkTypeId") Integer artworkTypeId,
            @RequestPart(name = "timePeriod", required = false) String timePeriod,
            @RequestPart(name = "rarityId") Integer rarityId,
            @RequestPart(name = "mediumId") Integer mediumId,
            @RequestPart(name = "sizeX", required = false) Double sizeX,
            @RequestPart(name = "sizeY", required = false) Double sizeY,
            @RequestPart(name = "sizeZ", required = false) Double sizeZ,
            @RequestPart(name = "materialId") Integer materialId,
            @RequestPart(name = "artworkLocation", required = false) String artworkLocation,
            @RequestPart(name = "artMovementId") Integer artMovementId,
            @RequestPart(name = "acquisitionWay") String acquisitionWay,
            @RequestPart(name = "artworkDescription", required = false) String artworkDescription,
            HttpServletRequest request
    ) {
        //@formatter:on
        try {

            User user = (User) request.getAttribute("user");
            Artwork artwork = Artwork.builder().userId(user.getUserId()).artworkName(artworkName).fixedPrice(fixedPrice)
                    .artworkTypeId(artworkTypeId).timePeriod(timePeriod).rarityId(rarityId).mediumId(mediumId).sizeX(sizeX).sizeY(sizeY)
                    .sizeZ(sizeZ).materialId(materialId).artworkLocation(artworkLocation).artMovementId(artMovementId)
                    .acquisitionWay(acquisitionWay).artworkDescription(artworkDescription).build();

            return ResponseEntity.ok(
                    ApiResponse.<UploadArtworkResponse>builder().operationResultData(artworkService.saveArtwork(artwork, image)).build());
        } catch (NullPointerException e) {
            throw new ArtworkException("Required information must be provided", HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            throw new ArtworkException("Invalid artwork status code", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/details/{artworkId}")
    public ResponseEntity<ApiResponse<ArtworkDisplayDetails>> getArtworkDisplayDetails(@PathVariable Integer artworkId) {
        return ResponseEntity.ok(ApiResponse.<ArtworkDisplayDetails>builder()
                .operationResultData(artworkService.getArtworkDisplayDetails(artworkId)).build());
    }

    @GetMapping("/search/{searchKey}")
    public ResponseEntity<ApiResponse<ArtworkSearchResponse>> searchArtwork(@PathVariable String searchKey) {
        return ResponseEntity
                .ok(ApiResponse.<ArtworkSearchResponse>builder().operationResultData(artworkService.searchArtwork(searchKey)).build());
    }

    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<ArtworkSearchResponse>> filterArtwork(@RequestBody GetFilteredArtworksRequest request) {
        //@formatter:off
        return ResponseEntity.ok(ApiResponse.<ArtworkSearchResponse>builder()
                .operationResultData(artworkService
                        .filterArtwork(ArtworkFilters.builder()
                                .mediumIds(request.getMediumId())
                                .materialIds(request.getMaterialId())
                                .rarityIds(request.getRarityId())
                                .artworkTypeIds(request.getArtworkTypeId())
                                .build()))
                .build());
        //@formatter:on
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<ArtworkSearchResponse>> getArtworks(HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        return ResponseEntity.ok(
                ApiResponse.<ArtworkSearchResponse>builder().operationResultData(artworkService.getArtworksOfArtistByUserId(user)).build());
    }

    @PutMapping("/{artworkId}")
    public ResponseEntity<?> updateArtworkDescription(@PathVariable Integer artworkId, @RequestBody DescriptionDto descriptionDto) {
        artworkService.updateArtworkDescription(artworkId, descriptionDto.getNewDescription());
        System.out.println(descriptionDto.getNewDescription());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/mediums")
    public ResponseEntity<ApiResponse<List<Medium>>> getMediums() {
        return ResponseEntity.ok(ApiResponse.<List<Medium>>builder().operationResultData(artworkService.getMediums()).build());
    }

    @GetMapping("/types")
    public ResponseEntity<ApiResponse<List<ArtworkType>>> getTypes() {
        return ResponseEntity.ok(ApiResponse.<List<ArtworkType>>builder().operationResultData(artworkService.getArtworkTypes()).build());
    }

    @GetMapping("/materials")
    public ResponseEntity<ApiResponse<List<Material>>> getMaterials() {
        return ResponseEntity.ok(ApiResponse.<List<Material>>builder().operationResultData(artworkService.getMaterials()).build());
    }

    @GetMapping("/rarities")
    public ResponseEntity<ApiResponse<List<Rarity>>> getRarities() {
        return ResponseEntity.ok(ApiResponse.<List<Rarity>>builder().operationResultData(artworkService.getRarities()).build());
    }

    @GetMapping("/filter_search/{searchKey}")
    public ResponseEntity<ApiResponse<ArtworkSearchResponse>> filterSearchArtwork(@PathVariable String searchKey,
            @RequestBody GetFilteredArtworksRequest request) {

        ArtworkFilters filters = ArtworkFilters.builder().mediumIds(request.getMediumId()).materialIds(request.getMaterialId())
                .rarityIds(request.getRarityId()).artworkTypeIds(request.getArtworkTypeId()).build();

        return ResponseEntity.ok(ApiResponse.<ArtworkSearchResponse>builder()
                .operationResultData(artworkService.filterSearchArtwork(searchKey, filters)).build());
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<ArtworkSearchResponse>> getAll() {
        return ResponseEntity.ok(ApiResponse.<ArtworkSearchResponse>builder().operationResultData(artworkService.getAllArtworks()).build());
    }

}