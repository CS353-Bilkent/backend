package com.backend.artbase.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Payment {

    private Integer paymentId;

    private Integer buyerId;

    private Integer sellerId;

    private Long bidId;

    private Integer workshopId;

    private Boolean approved;

    private Character paymentType;

}
