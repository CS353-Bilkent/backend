package com.backend.artbase.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.artbase.entities.ApiResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/auction")
@RequiredArgsConstructor
public class AuctionController {

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Integer>> postMethodName(@RequestBody Integer entity) {
        return null;
    }

}
