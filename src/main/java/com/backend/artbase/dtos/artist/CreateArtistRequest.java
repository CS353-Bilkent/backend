package com.backend.artbase.dtos.artist;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateArtistRequest {

    @NonNull
    private String artistName;

    private String gender;

    private String nationality;

    private Integer age;

    private String speciality;
}
