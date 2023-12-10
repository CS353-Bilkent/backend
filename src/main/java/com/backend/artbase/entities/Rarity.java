package com.backend.artbase.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Rarity {

    @NonNull
    private Integer rarityId;

    @NonNull
    private String rarityName;

}
