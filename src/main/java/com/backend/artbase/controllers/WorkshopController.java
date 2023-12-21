package com.backend.artbase.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.backend.artbase.dtos.workshop.WorkshopDto;
import com.backend.artbase.services.WorkshopService;
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
}