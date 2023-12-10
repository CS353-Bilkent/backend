package com.backend.artbase.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.backend.artbase.dtos.artwork.UploadArtworkResponse;
import com.backend.artbase.dtos.auth.AuthResponse;
import com.backend.artbase.entities.ApiResponse;
import com.backend.artbase.entities.Artwork;
import com.backend.artbase.errors.ArtworkException;
import com.backend.artbase.services.ArtworkService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/art")
public class ArtworkController {

    private final ArtworkService artworkService;

    //@formatter:off
@PostMapping("/upload")
    public ResponseEntity<ApiResponse<UploadArtworkResponse>> saveArtwork(
            @RequestPart(name = "image") MultipartFile[] images,
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

            Artwork artwork = Artwork.builder().userId(userId).artistId(artistId).fixedPrice(fixedPrice).artworkTypeId(artworkTypeId)
                    .timePeriod(timePeriod).rarityId(rarityId).mediumId(mediumId).sizeX(sizeX).sizeY(sizeY).sizeZ(sizeZ)
                    .materialId(materialId).artworkLocation(artworkLocation).artMovementId(artMovementId).acquisitionWay(acquisitionWay)
                    .artworkDescription(artworkDescription).build();

            artworkService.saveArtwork(artwork, images);

        } catch (NullPointerException e) {
            throw new ArtworkException("Required information must be provided", HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(ApiResponse.<UploadArtworkResponse>builder().operationResultData(null).build());
    }
}
