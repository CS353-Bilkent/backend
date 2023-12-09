package com.backend.artbase.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ArtMovement {

    @NonNull
    private Integer artMovementId;

    @NonNull
    private String artMovementName;

}
