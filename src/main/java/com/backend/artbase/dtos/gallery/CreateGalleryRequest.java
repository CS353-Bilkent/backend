package com.backend.artbase.dtos.gallery;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateGalleryRequest {

    @NonNull
    private String artGalleryName;

    @NonNull
    private String artGalleryLocation;
}
