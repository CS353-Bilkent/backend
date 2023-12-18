package com.backend.artbase.dtos.artwork;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ArtworkSearchResponse {

    private List<ArtworkDisplayDetails> artworkDtos;
}
