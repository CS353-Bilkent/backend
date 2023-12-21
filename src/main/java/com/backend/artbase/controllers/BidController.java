package com.backend.artbase.controllers;

import com.backend.artbase.dtos.bid.BidRequest;
import com.backend.artbase.entities.ApiResponse;
import com.backend.artbase.entities.Bid;
import com.backend.artbase.entities.User;
import com.backend.artbase.services.BidService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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

    @PostMapping("/{auctionId}")
    public ResponseEntity<ApiResponse<Long>> makeBid(@PathVariable Integer auctionId, @RequestBody BidRequest bidRequest,
            HttpServletRequest request) {
        System.out.println("bid" + bidRequest);
        User user = (User) request.getAttribute("user");
        return ResponseEntity.ok(ApiResponse.<Long>builder().operationResultData(bidService.makeBid(bidRequest.getAmount(), user, auctionId)).build());
    }
}
