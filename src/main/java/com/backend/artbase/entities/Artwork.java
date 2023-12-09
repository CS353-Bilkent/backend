package com.backend.artbase.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Artwork {

    @NonNull
    private Integer userId;

    @NonNull
    private Integer artistId;

    @NonNull
    private Integer artworkId;

    private Double fixedPrice;

    @NonNull
    private Integer artworkTypeId;

    private String timePeriod;

    @NonNull
    private Integer rarityId;

    @NonNull
    private Integer mediumId;

    private Double sizeX;

    private Double sizeY;

    private Double sizeZ;

    @NonNull
    private Integer materialId;

    private String artworkLocation;

    @NonNull
    private Integer artMovementId;

    @NonNull
    private String acquisitionWay;

    private String artworkDescription;

}