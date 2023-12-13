package com.backend.artbase.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.artbase.dtos.collection.CollectionDto;
import com.backend.artbase.dtos.collection.CreateCollectionRequest;
import com.backend.artbase.dtos.collection.GetCollectionsResponse;
import com.backend.artbase.dtos.collection.SaveArtworksToCollectionRequest;
import com.backend.artbase.entities.ApiResponse;
import com.backend.artbase.entities.User;
import com.backend.artbase.services.CollectionService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequiredArgsConstructor
@RequestMapping("/collection")
public class CollectionController {

    private final CollectionService collectionService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<?>> createCollection(@RequestBody CreateCollectionRequest createRequest, HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        collectionService.createCollection(user.getUserId(), createRequest.getArtworkCollectionName(),
                createRequest.getArtworkCollectionDescription());
        return ResponseEntity.ok(ApiResponse.builder().build());

    }

    @GetMapping("/{collectionId}")
    public ResponseEntity<ApiResponse<CollectionDto>> getCollection(@PathVariable Integer collectionId) {
        return ResponseEntity
                .ok(ApiResponse.<CollectionDto>builder().operationResultData(collectionService.getCollection(collectionId)).build());
    }

    @PutMapping("/{collectionId}")
    public ResponseEntity<ApiResponse<?>> saveArtworksToCollection(@PathVariable Integer collectionId,
            @RequestBody SaveArtworksToCollectionRequest collectionRequest, HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        collectionService.saveArtworksToCollection(user.getUserId(), collectionRequest.getArtworks(), collectionId);
        return ResponseEntity.ok(ApiResponse.builder().build());
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<GetCollectionsResponse>> getCollections() {
        return ResponseEntity.ok(ApiResponse.<GetCollectionsResponse>builder()
                .operationResultData(GetCollectionsResponse.builder().collections(collectionService.getAllCollections()).build()).build());
    }

    @GetMapping("/creator/{creatorId}")
    public ResponseEntity<ApiResponse<GetCollectionsResponse>> getCollectionsByCreatorId(@PathVariable Integer creatorId) {
        return ResponseEntity.ok(ApiResponse.<GetCollectionsResponse>builder()
                .operationResultData(
                        GetCollectionsResponse.builder().collections(collectionService.getCollectionsByCreatorId(creatorId)).build())
                .build());
    }

    @PostMapping("/delete/{collectionId}")
    public ResponseEntity<ApiResponse<?>> deleteCollection(@PathVariable Integer collectionId, HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        collectionService.deleteCollection(user.getUserId(), collectionId);
        return ResponseEntity.ok(ApiResponse.builder().build());
    }

}
