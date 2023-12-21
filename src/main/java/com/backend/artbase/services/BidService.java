package com.backend.artbase.services;

import com.backend.artbase.daos.BidDao;
import com.backend.artbase.entities.Bid;
import com.backend.artbase.entities.User;
import com.backend.artbase.errors.AuctionRuntimeException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BidService {

    @Autowired
    private BidDao bidDao;

    public List<Bid> getBidsForArtwork(Integer artworkId) {
        return bidDao.findBidsByArtworkId(artworkId);
    }

    public Bid approveBid(Integer bidId, User user) {
        Bid bid = bidDao.findById(bidId)
                .orElseThrow(() -> new AuctionRuntimeException("Bid not found with id: " + bidId, HttpStatus.NOT_FOUND));

        Integer ownerId = bidDao.getAuctionOwner(bidId);

        if (ownerId != user.getUserId()) {
            throw new AuctionRuntimeException("Only owner of the auction can approve a bid", HttpStatus.UNAUTHORIZED);
        }

        bid.setBidStatus(true);
        return bidDao.save(bid);
    }

    public Bid rejectBid(Integer bidId, User user) {
        Bid bid = bidDao.findById(bidId)
                .orElseThrow(() -> new AuctionRuntimeException("Bid not found with id: " + bidId, HttpStatus.NOT_FOUND));

        Integer ownerId = bidDao.getAuctionOwner(bidId);

        if (ownerId != user.getUserId()) {
            throw new AuctionRuntimeException("Only owner of the auction can reject a bid", HttpStatus.UNAUTHORIZED);
        }

        bid.setBidStatus(false);
        return bidDao.save(bid);
    }

    public Long makeBid(BigDecimal amount, User user, Integer auctionId) {

        Long bidId = bidDao.getNextBidId();

        Bid bid = Bid.builder().auctionId(auctionId).userId(user.getUserId()).bidAmount(amount).bidId(bidId).bidTime(LocalDateTime.now())
                .build();
        bidDao.makeBid(bid);
        return bidId;
    }
}
