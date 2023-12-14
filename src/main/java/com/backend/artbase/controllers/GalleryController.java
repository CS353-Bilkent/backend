package com.backend.artbase.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.artbase.dtos.artist.CreateArtistRequest;
import com.backend.artbase.dtos.artist.CreateArtistResponse;
import com.backend.artbase.dtos.gallery.CreateGalleryRequest;
import com.backend.artbase.dtos.gallery.CreateGalleryResponse;
import com.backend.artbase.entities.ApiResponse;
import com.backend.artbase.entities.User;
import com.backend.artbase.services.GalleryService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/gallery")
public class GalleryController {

    private final GalleryService galleryService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<CreateGalleryResponse>> createGallery(@RequestBody CreateGalleryRequest createGalleryRequest,
            HttpServletRequest request) {

        User user = (User) request.getAttribute("user");

        return ResponseEntity.ok(ApiResponse.<CreateGalleryResponse>builder()
                .operationResultData(
                        CreateGalleryResponse.builder().galleryId(galleryService.createGallery(createGalleryRequest, user)).build())
                .build());

    }
}
