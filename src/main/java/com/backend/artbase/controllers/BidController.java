package com.backend.artbase.controllers;

import com.backend.artbase.entities.Bid;
import com.backend.artbase.services.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bid")
public class BidController {

    @Autowired
    private BidService bidService;

    @GetMapping("/artwork/{artworkId}/bids")
    public ResponseEntity<List<Bid>> getBidsForArtwork(@PathVariable Integer artworkId) {
        List<Bid> bids = bidService.getBidsForArtwork(artworkId);
        return ResponseEntity.ok(bids);
    }

    @PutMapping("/approve/{bidId}")
    public ResponseEntity<?> approvePayment(@PathVariable Integer bidId) {
        try {
            Bid bid = bidService.approveBid(bidId);
            return ResponseEntity.ok(bid);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/reject/{bidId}")
    public ResponseEntity<?> rejectPayment(@PathVariable Integer bidId) {
        try {
            Bid bid = bidService.rejectBid(bidId);
            return ResponseEntity.ok(bid);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
