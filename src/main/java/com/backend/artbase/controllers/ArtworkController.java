package com.backend.artbase.controllers;

import com.backend.artbase.dtos.artwork.ArtworkSearchResponse;
import com.backend.artbase.entities.ApiResponse;
import com.backend.artbase.entities.Artwork;
import com.backend.artbase.entities.ArtworkFilters;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.backend.artbase.dtos.artwork.GetArtworkDisplayDetailsResponse;
import com.backend.artbase.dtos.artwork.GetFilteredArtworksRequest;
import com.backend.artbase.dtos.artwork.UploadArtworkResponse;
import com.backend.artbase.errors.ArtworkException;
import com.backend.artbase.services.ArtworkService;
import lombok.RequiredArgsConstructor;

import java.util.List;

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
            @RequestPart(name = "userId") Integer userId,
            @RequestPart(name = "artistId") Integer artistId,
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
            @RequestPart(name = "artworkDescription", required = false) String artworkDescription
    ) {
    //@formatter:on
        try {

            Artwork artwork = Artwork.builder().userId(userId).artistId(artistId).artworkName(artworkName).fixedPrice(fixedPrice)
                    .artworkTypeId(artworkTypeId).timePeriod(timePeriod).rarityId(rarityId).mediumId(mediumId).sizeX(sizeX).sizeY(sizeY)
                    .sizeZ(sizeZ).materialId(materialId).artworkLocation(artworkLocation).artMovementId(artMovementId)
                    .acquisitionWay(acquisitionWay).artworkDescription(artworkDescription).build();

            return ResponseEntity.ok(
                    ApiResponse.<UploadArtworkResponse>builder().operationResultData(artworkService.saveArtwork(artwork, image)).build());
        } catch (NullPointerException e) {
            throw new ArtworkException("Required information must be provided", HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/addImage/{artworkId}")
    public ResponseEntity<ApiResponse<?>> addImageToArtwork(@PathVariable Integer artworkId, @RequestPart MultipartFile image) {
        artworkService.addImageToArtwork(image, artworkId);
        return ResponseEntity.ok(ApiResponse.builder().build());
    }

    @GetMapping("/details/{artworkId}")
    public ResponseEntity<ApiResponse<GetArtworkDisplayDetailsResponse>> getArtworkDisplayDetails(@PathVariable Integer artworkId) {
        return ResponseEntity.ok(ApiResponse.<GetArtworkDisplayDetailsResponse>builder()
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

    @GetMapping("/filter_search/{searchKey}")
    public ResponseEntity<ApiResponse<ArtworkSearchResponse>> filterSearchArtwork(@PathVariable String searchKey,
            @RequestBody GetFilteredArtworksRequest request) {

        ArtworkFilters filters = ArtworkFilters.builder().mediumIds(request.getMediumId()).materialIds(request.getMaterialId())
                .rarityIds(request.getRarityId()).artworkTypeIds(request.getArtworkTypeId()).build();

        return ResponseEntity.ok(ApiResponse.<ArtworkSearchResponse>builder()
                .operationResultData(artworkService.filterSearchArtwork(searchKey, filters)).build());
    }

    @GetMapping
    public ResponseEntity<?> getArtworks(@RequestParam Integer artistId) {
        return ResponseEntity.ok(artworkService.getArtworksOfArtist(artistId));
    }

    @PutMapping("/{artworkId}")
    public ResponseEntity<?> updateArtworkDescription(@PathVariable Integer artworkId, @RequestBody String newDescription) {
        artworkService.updateArtworkDescription(artworkId, newDescription);
        return ResponseEntity.ok().build();
    }
    
}
