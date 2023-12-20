package com.backend.artbase.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.artbase.daos.ArtworkDao;
import com.backend.artbase.daos.AuctionDao;
import com.backend.artbase.dtos.artwork.ArtworkDto;
import com.backend.artbase.dtos.auction.AuctionDto;
import com.backend.artbase.dtos.auction.CreateAuctionRequest;
import com.backend.artbase.entities.Auction;
import com.backend.artbase.entities.User;
import com.backend.artbase.errors.AuctionRuntimeException;

import lombok.RequiredArgsConstructor;

@Transactional
@Service
@RequiredArgsConstructor
public class AuctionService {

    private final AuctionDao auctionDao;
    private final ArtworkService artworkService;

    public Integer createAuction(User user, CreateAuctionRequest createRequest) {
        Integer nextAuctionId = auctionDao.getNextAuctionId();

        ArtworkDto artworkDto = artworkService.getArtwork(createRequest.getArtworkId());

        if (artworkDto.getUserId() != user.getUserId()) {
            throw new AuctionRuntimeException("Only owner of artwork can start an auction", HttpStatus.UNAUTHORIZED);
        }

        Auction auction = Auction.builder().artworkId(artworkDto.getArtworkId()).auctionEndDate(createRequest.getAuctionEndDate())
                .auctionStartDate(createRequest.getAuctionStartDate()).auctionId(nextAuctionId).userId(user.getUserId())
                .initialBid(createRequest.getInitialBid()).build();

        auctionDao.createNewAuction(auction);
        return nextAuctionId;
    }

    public List<AuctionDto> getAllAuctions() {
        return getAuctionDtos(auctionDao.getAllAuctions());
    }

    public List<AuctionDto> getUserAuctions(Integer userId) {
        return getAuctionDtos(auctionDao.getUserAuctions(userId));
    }

    public List<AuctionDto> getInactiveAuctions() {
        return getAuctionDtos(auctionDao.getInactiveAuctions());
    }

    public AuctionDto getAuctionOfArtwork(Integer artworkId) {

        Optional<Auction> optAuction = auctionDao.getAuctionForArtwork(artworkId);
        if (optAuction.isEmpty()) {
            throw new AuctionRuntimeException("Auction cannot found for artwork with ID: " + artworkId, HttpStatus.NOT_FOUND);
        }

        return AuctionDto.builder().auction(optAuction.get()).artwork(artworkService.getArtworkDisplayDetails(artworkId)).build();
    }

    private List<AuctionDto> getAuctionDtos(List<Auction> auctions) {
        List<AuctionDto> auctionDtos = new ArrayList<>();

        auctions.forEach(e -> {
            auctionDtos.add(AuctionDto.builder().auction(e).artwork(artworkService.getArtworkDisplayDetails(e.getArtworkId())).build());
        });

        return auctionDtos;
    }

}
