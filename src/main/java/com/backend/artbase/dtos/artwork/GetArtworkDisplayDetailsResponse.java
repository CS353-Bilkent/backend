package com.backend.artbase.dtos.artwork;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@lombok.Builder
public class GetArtworkDisplayDetailsResponse {

    private ArtworkDto artworkDto;
    private byte[] displayImage;
}
