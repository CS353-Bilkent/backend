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
public class Bid {

    @NonNull
    private Long bidId;

    @NonNull
    private Integer auctionId;

    @NonNull
    private Integer userId;

    @NonNull
    private BigDecimal bidAmount;

    private Boolean bidStatus;

    @NonNull
    private LocalDateTime bidTime;

}
