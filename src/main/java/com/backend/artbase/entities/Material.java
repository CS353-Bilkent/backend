package com.backend.artbase.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Material {

    @NonNull
    private Integer materialId;

    @NonNull
    private String materialName;

}
