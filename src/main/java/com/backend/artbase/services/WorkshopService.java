package com.backend.artbase.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.artbase.daos.WorkshopDao;
import com.backend.artbase.dtos.workshop.CreateWorkshopRequest;
import com.backend.artbase.dtos.workshop.WorkshopDto;
import com.backend.artbase.entities.Artist;
import com.backend.artbase.entities.User;
import com.backend.artbase.entities.Workshop;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class WorkshopService {

    private final WorkshopDao workshopDao;
    private final ArtistService artistService;

    public List<WorkshopDto> getAllWorkshops() {
        return workshopDao.findAll();
    }

    public Integer createWorkshop(CreateWorkshopRequest request, User user) {
        Integer workId = workshopDao.getNextWorkshopId();
        Artist artist = artistService.getArtistByUserId(user.getUserId());

        Workshop workshop = Workshop.builder().workshopId(workId).artistId(artist.getArtistId())
                .workshopDescription(request.getWorkshopDescription()).dateTime(request.getDateTime()).duration(request.getDuration())
                .mediumId(request.getMediumId()).price(request.getPrice()).capacity(request.getCapacity()).title(request.getTitle())
                .workshopType(request.getWorkshopType()).build();

        workshopDao.saveWorkshop(workshop);

        return workId;
    }

    public List<WorkshopDto> getOrganizedWorkshops(User user) {
        Artist artist = artistService.getArtistByUserId(user.getUserId());

        return workshopDao.findByArtistId(artist.getArtistId());
    }

    public List<WorkshopDto> getWorkshopsParticipated(User user) {
        return workshopDao.getParticipatedWorkshops(user.getUserId());
    }

    public void participateToWorkshop(Integer workshopId, User user) {
        workshopDao.participateToWorkshop(user.getUserId(), workshopId);
    }

}