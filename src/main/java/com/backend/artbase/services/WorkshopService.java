package com.backend.artbase.services;

import org.springframework.stereotype.Service;

import com.backend.artbase.daos.WorkshopDao;
import com.backend.artbase.dtos.workshop.WorkshopDto;

import java.util.List;

@Service
public class WorkshopService {

    private final WorkshopDao workshopDao;

    public WorkshopService(WorkshopDao workshopDao) {
        this.workshopDao = workshopDao;
    }

    public List<WorkshopDto> getAllWorkshops() {
        return workshopDao.findAll();
    }
}