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
}
