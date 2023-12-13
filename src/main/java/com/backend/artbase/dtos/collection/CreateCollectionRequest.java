package com.backend.artbase.dtos.collection;

import com.google.auto.value.AutoValue.Builder;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateCollectionRequest {

    @NonNull
    private String artworkCollectionName;

    private String artworkCollectionDescription;
}
