package com.backend.artbase.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ArtGallery {

    @NonNull
    private Integer artGalleryId;

    @NonNull
    private Integer userId;

    @NonNull
    private String artGalleryName;

    @NonNull
    private String artGalleryLocation;

}
