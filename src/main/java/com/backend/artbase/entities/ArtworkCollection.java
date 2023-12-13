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
    private Integer collectionId;

    @NonNull
    private String collectionName;

    private String collectionDescription;

    @NonNull
    private Integer creatorId;

}
