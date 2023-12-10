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
public class Auction {

    @NonNull
    private Integer userId;

    @NonNull
    private Integer auctionId;

    @NonNull
    private LocalDateTime auctionStartDate;

    @NonNull
    private LocalDateTime auctionEndDate;

    @NonNull
    private Integer artworkId;

    @NonNull
    private BigDecimal initialBid;

    private Boolean active;

}
