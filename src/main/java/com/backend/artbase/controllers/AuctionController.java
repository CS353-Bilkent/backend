package com.backend.artbase.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.artbase.dtos.auction.AuctionDto;
import com.backend.artbase.dtos.auction.CreateAuctionRequest;
import com.backend.artbase.entities.ApiResponse;
import com.backend.artbase.entities.User;
import com.backend.artbase.services.AuctionService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<AuctionDto>>> getAllAuctions() {
        List<AuctionDto> auctions = auctionService.getAllAuctions();
        return ResponseEntity.ok(ApiResponse.<List<AuctionDto>>builder().operationResultData(auctions).build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<AuctionDto>>> getUserAuctions(@PathVariable Integer userId) {
        List<AuctionDto> userAuctions = auctionService.getUserAuctions(userId);
        return ResponseEntity.ok(ApiResponse.<List<AuctionDto>>builder().operationResultData(userAuctions).build());
    }

    @GetMapping("/inactive")
    public ResponseEntity<ApiResponse<List<AuctionDto>>> getInactiveAuctions() {
        List<AuctionDto> inactiveAuctions = auctionService.getInactiveAuctions();
        return ResponseEntity.ok(ApiResponse.<List<AuctionDto>>builder().operationResultData(inactiveAuctions).build());
    }

    @GetMapping("/artwork/{artworkId}")
    public ResponseEntity<ApiResponse<AuctionDto>> getAuctionOfArtwork(@PathVariable Integer artworkId) {
        AuctionDto auctionDto = auctionService.getAuctionOfArtwork(artworkId);
        return ResponseEntity.ok(ApiResponse.<AuctionDto>builder().operationResultData(auctionDto).build());
    }

}
