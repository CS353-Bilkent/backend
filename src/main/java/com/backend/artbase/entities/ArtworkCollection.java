package com.backend.artbase.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ArtworkCollection {

    @NonNull
    private Integer artworkCollectionId;

    @NonNull
    private String artworkCollectionName;

    private String artworkCollectionDescription;

    @NonNull
    private Integer creatorId;

}
