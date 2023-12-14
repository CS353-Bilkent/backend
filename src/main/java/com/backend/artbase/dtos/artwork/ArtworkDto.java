package com.backend.artbase.dtos.artwork;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ArtworkDto {

    //Artwork attributes
    private String artworkName;
    private Integer userId;
    private Integer artistId;
    private Integer artworkId;
    private Double fixedPrice;
    private Integer artworkTypeId;
    private String timePeriod;
    private Integer rarityId;
    private Integer mediumId;
    private Double sizeX;
    private Double sizeY;
    private Double sizeZ;
    private Integer materialId;
    private String artworkLocation;
    private Integer artMovementId;
    private String acquisitionWay;
    private String artworkDescription;
    private String artworkStatus;

    //Artist attributes (userId and artistId omitted)
    private String artistName;
    private String gender;
    private String nationality;
    private Integer age;
    private String speciality;
}
