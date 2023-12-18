package com.backend.artbase.controllers;

import com.backend.artbase.entities.Bid;
import com.backend.artbase.entities.User;
import com.backend.artbase.services.BidService;

import jakarta.servlet.http.HttpServletRequest;

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
    public ResponseEntity<?> approvePayment(@PathVariable Integer bidId, HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        Bid bid = bidService.approveBid(bidId, user);
        return ResponseEntity.ok(bid);

    }

    @PutMapping("/reject/{bidId}")
    public ResponseEntity<?> rejectPayment(@PathVariable Integer bidId, HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        Bid bid = bidService.rejectBid(bidId, user);
        return ResponseEntity.ok(bid);

    }
}
