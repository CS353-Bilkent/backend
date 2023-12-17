package com.backend.artbase.services;

import com.backend.artbase.daos.BidDao;
import com.backend.artbase.entities.Bid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BidService {

    @Autowired
    private BidDao bidDao;

    public List<Bid> getBidsForArtwork(Integer artworkId) {
        return bidDao.findBidsByArtworkId(artworkId);
    }

    public Bid approveBid(Integer bidId) throws Exception {
        Bid bid = bidDao.findById(bidId)
                .orElseThrow(() -> new Exception("Bid not found with id: " + bidId));
        
        bid.setBidStatus(true);
        return bidDao.save(bid);
    }

    public Bid rejectBid(Integer bidId) throws Exception {
        Bid bid = bidDao.findById(bidId)
            .orElseThrow(() -> new Exception("Bid not found with id: " + bidId));

        bid.setBidStatus(false);
        return bidDao.save(bid);
    }
}
