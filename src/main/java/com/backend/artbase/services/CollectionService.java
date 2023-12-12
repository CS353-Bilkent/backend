package com.backend.artbase.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.artbase.daos.CollectionDao;
import com.backend.artbase.dtos.collection.CollectionDto;

import lombok.RequiredArgsConstructor;

@Transactional
@Service
@RequiredArgsConstructor
public class CollectionService {

    private final CollectionDao collectionDao;

    public Integer createCollection(Integer userId, String collectionName, String collectionDesc) {
        return 0;
    }

    public void saveArtworksToCollection(Integer userId, Integer[] artworks) {

    }

    public CollectionDto getCollection(Integer collectionId) {
        return null;
    }

    public List<CollectionDto> getCollectionsByCreatorId(Integer creatorId) {
        return null;
    }

    public List<CollectionDto> getCollections() {
        return null;
    }

    public void deleteCollection(Integer userId, String collectionId) {
    }
}
