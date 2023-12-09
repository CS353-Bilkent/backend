package com.backend.artbase.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class Workshop {

    @NonNull
    private Integer workshopId;

    @NonNull
    private Integer artistId;

    @NonNull
    private String workshopDescription;

    @NonNull
    private LocalDateTime dateTime;

    @NonNull
    private Integer duration;

    @NonNull
    private Integer mediumId;

    @NonNull
    private BigDecimal price;

    @NonNull
    private Integer capacity;

    @NonNull
    private String title;

    @NonNull
    private String workshopType;

}
