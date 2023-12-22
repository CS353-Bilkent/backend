package com.backend.artbase.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.artbase.dtos.workshop.CreateWorkshopRequest;
import com.backend.artbase.dtos.workshop.WorkshopDto;
import com.backend.artbase.entities.ApiResponse;
import com.backend.artbase.entities.User;
import com.backend.artbase.services.WorkshopService;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

@RestController
@RequestMapping("/workshop")
public class WorkshopController {

    private final WorkshopService workshopService;

    public WorkshopController(WorkshopService workshopService) {
        this.workshopService = workshopService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<WorkshopDto>> getAllWorkshops() {
        List<WorkshopDto> workshops = workshopService.getAllWorkshops();
        return ResponseEntity.ok(workshops);
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Integer>> createWorkshop(@RequestBody CreateWorkshopRequest createReq, HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        return ResponseEntity
                .ok(ApiResponse.<Integer>builder().operationResultData(workshopService.createWorkshop(createReq, user)).build());
    }

    @PostMapping("/participate/{workshopId}")
    public ResponseEntity<?> participateWorkshop(@PathVariable Integer workshopId, HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        workshopService.participateToWorkshop(workshopId, user);
        return ResponseEntity.ok(1);
    }

    @GetMapping("/organized")
    public ResponseEntity<ApiResponse<List<WorkshopDto>>> getOrganizedWorkshops(HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        List<WorkshopDto> organizedWorkshops = workshopService.getOrganizedWorkshops(user);
        return ResponseEntity.ok(ApiResponse.<List<WorkshopDto>>builder().operationResultData(organizedWorkshops).build());
    }

    @GetMapping("/participated")
    public ResponseEntity<ApiResponse<List<WorkshopDto>>> getWorkshopsParticipated(HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        List<WorkshopDto> participatedWorkshops = workshopService.getWorkshopsParticipated(user);
        return ResponseEntity.ok(ApiResponse.<List<WorkshopDto>>builder().operationResultData(participatedWorkshops).build());
    }
}