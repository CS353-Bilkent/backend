package com.backend.artbase.dtos.workshop;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.google.cloud.Timestamp;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateWorkshopRequest {

    private String workshopDescription;

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