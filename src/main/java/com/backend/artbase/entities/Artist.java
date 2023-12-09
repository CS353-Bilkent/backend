package com.backend.artbase.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Artist {

    @NonNull
    private Integer userId;

    @NonNull
    private Integer artistId;

    @NonNull
    private String artistName;

    private String gender;

    private String nationality;

    private Integer age;

    private String speciality;

}
