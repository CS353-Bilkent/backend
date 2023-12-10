package com.backend.artbase.entities;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ArtworkFilters {
    // ignores initialized values
    private List<Integer> mediumIds = new ArrayList<>();
    private List<Integer> materialIds = new ArrayList<>();
    private List<Integer> rarityIds = new ArrayList<>();
    private List<Integer> artworkTypeIds = new ArrayList<>();
}
