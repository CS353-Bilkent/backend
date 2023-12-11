package com.backend.artbase.dtos.artwork;

import com.backend.artbase.entities.Artwork;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@lombok.Builder
public class GetArtworkDisplayDetailsResponse {

    private Artwork artwork;
    private byte[] displayImage;
}
