package com.backend.artbase.dtos.artist;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateArtistResponse {

    private Integer artistId;
}
