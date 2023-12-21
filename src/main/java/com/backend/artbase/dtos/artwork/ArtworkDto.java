package com.backend.artbase.dtos.artwork;

import com.backend.artbase.entities.ArtworkStatus;
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
    private String artworkTypeName;
    private String timePeriod;
    private String rarityName;
    private String mediumName;
    private Double sizeX;
    private Double sizeY;
    private Double sizeZ;
    private String materialName;
    private String artworkLocation;
    private String artMovementName;
    private String acquisitionWay;
    private String artworkDescription;
    private ArtworkStatus artworkStatus;

    //Artist attributes (userId and artistId omitted)
    private String artistName;
    private String gender;
    private String nationality;
    private Integer age;
    private String speciality;
}