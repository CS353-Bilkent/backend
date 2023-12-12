package com.backend.artbase.dtos.collection;

import java.util.Collection;
import java.util.List;

import com.backend.artbase.entities.Artwork;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CollectionDto {

    private Collection collection;
    // CONVERT THİS TO ARTWORK DTO
    private List<Artwork> artworks;
}
