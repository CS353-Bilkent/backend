package com.backend.artbase.dtos.collection;

import java.util.List;

import com.backend.artbase.dtos.artwork.ArtworkDisplayDetails;
import com.backend.artbase.dtos.artwork.ArtworkDto;
import com.backend.artbase.entities.ArtworkCollection;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CollectionDto {

    private ArtworkCollection collection;
    private List<ArtworkDisplayDetails> artworkDtos;
}
