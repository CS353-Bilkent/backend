package com.backend.artbase.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.artbase.dtos.artist.CreateArtistRequest;
import com.backend.artbase.dtos.artist.CreateArtistResponse;
import com.backend.artbase.entities.ApiResponse;
import com.backend.artbase.entities.User;
import com.backend.artbase.services.ArtistService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("/artist")
public class ArtistController {

    private final ArtistService artistService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<CreateArtistResponse>> createArtist(@RequestBody CreateArtistRequest createArtistRequest,
            HttpServletRequest request) {

        User user = (User) request.getAttribute("user");

        return ResponseEntity.ok(ApiResponse.<CreateArtistResponse>builder()
                .operationResultData(CreateArtistResponse.builder().artistId(artistService.createArtist(createArtistRequest, user)).build())
                .build());

    }

}
