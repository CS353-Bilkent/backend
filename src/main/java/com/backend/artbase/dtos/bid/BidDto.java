package com.backend.artbase.dtos.bid;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BidDto {
    private Long bidId;
    private Integer auctionId;
    private Integer userId;
    private BigDecimal bidAmount;
    private Boolean bidStatus;
    private LocalDateTime bidTime;
}
