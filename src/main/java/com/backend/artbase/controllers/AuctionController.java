package com.backend.artbase.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.artbase.dtos.auction.CreateAuctionRequest;
import com.backend.artbase.entities.ApiResponse;
import com.backend.artbase.entities.User;
import com.backend.artbase.services.AuctionService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/auction")
@RequiredArgsConstructor
public class AuctionController {

    private final AuctionService auctionService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Integer>> createAuctionForArtwork(@RequestBody CreateAuctionRequest createRequest,
            HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        return ResponseEntity
                .ok(ApiResponse.<Integer>builder().operationResultData(auctionService.createAuction(user, createRequest)).build());
    }

}
