package com.backend.artbase.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.artbase.entities.ApiResponse;

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

    @GetMapping("/{collectionId}")
    public ResponseEntity<ApiResponse<?>> getCollection(@PathVariable String param) {
        return null;
    }

    @PutMapping("/{collectionId}")
    public ResponseEntity<ApiResponse<?>> saveArtworksToCollection(@PathVariable String param) {
        return null;
    }

    @PostMapping("/delete/{collectionId}")
    public ResponseEntity<ApiResponse<?>> deleteCollection(@PathVariable String param) {
        return null;
    }

    @GetMapping("/collections")
    public ResponseEntity<ApiResponse<?>> getCollections(@RequestBody String param) {
        return null;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<?>> createCollection(@RequestBody String dummy) {
        return null;
    }

}
