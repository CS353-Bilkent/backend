package com.backend.artbase.dtos.artwork;

import java.util.List;

import com.backend.artbase.entities.Artwork;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@lombok.Builder
public class GetArtworkFullDetailsResponse {

    private ArtworkDto artworkDto;
    private List<byte[]> displayImages;
}
