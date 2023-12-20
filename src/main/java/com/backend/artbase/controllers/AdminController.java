package com.backend.artbase.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    /*
     * TODO Approve Auction Approve Payment Look up old auctions, sysdate > end
     * date look up bid statistics per artwork, how many bid, max value, length
     * of auction etc. use group by, max, count Look up user statistics, artwork
     * group by artist, Look up workshops //Workshopları atıcam yarın Look up
     * collection statistics, artwork added to any collection group by artist
     * Look up old sales etc
     */

}
