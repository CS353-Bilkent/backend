package com.backend.artbase.dtos.collection;

import com.google.auto.value.AutoValue.Builder;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SaveArtworksToCollectionRequest {

    Integer[] artworks;

}
