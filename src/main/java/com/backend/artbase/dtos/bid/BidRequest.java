package com.backend.artbase.dtos.bid;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BidRequest {
    private BigDecimal amount;
}
