package com.backend.artbase.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.artbase.entities.ApiResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("/artist")
public class ArtistController {

    @PostMapping("")
    public ResponseEntity<ApiResponse<?>> createArtist(@RequestBody Integer entity) {

        return null;

    }

}
