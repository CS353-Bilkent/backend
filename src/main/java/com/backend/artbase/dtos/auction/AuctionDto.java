package com.backend.artbase.dtos.auction;

import com.backend.artbase.dtos.artwork.ArtworkDisplayDetails;
import com.backend.artbase.entities.Auction;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuctionDto {

    private ArtworkDisplayDetails artwork;
    private Auction auction;
}
