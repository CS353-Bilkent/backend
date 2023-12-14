package com.backend.artbase.dtos.artwork;

import com.backend.artbase.entities.Artwork;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ArtworkSearchResponse {

    //TODO: bu iki list yerine direkt ArtworkDto listesi dönmek daha mantıklı olabilir
    private List<Artwork> artworks;
    private List<String> artistNames;
}
