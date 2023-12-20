package com.backend.artbase.dtos.auction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.google.auto.value.AutoValue.Builder;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateAuctionRequest {

    @NonNull
    private LocalDateTime auctionStartDate;

    @NonNull
    private LocalDateTime auctionEndDate;

    @NonNull
    private Integer artworkId;

    @NonNull
    private BigDecimal initialBid;
}
