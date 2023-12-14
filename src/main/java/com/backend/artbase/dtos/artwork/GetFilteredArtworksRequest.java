package com.backend.artbase.dtos.artwork;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GetFilteredArtworksRequest {

    private List<Integer> mediumId;
    private List<Integer> materialId;
    private List<Integer> rarityId;
    private List<Integer> artworkTypeId;
}
