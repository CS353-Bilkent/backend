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
        /*
         * check if user exists, get next colleciton id, null checks, create,
         * return id
         */
        return 0;
    }

    public void saveArtworksToCollection(Integer userId, Integer[] artworks, String collectionId) {
        /*
         * check if creator id and user id is same, check artworks exist, add
         * artwork to colleciton and save
         */

    }

    public CollectionDto getCollection(Integer collectionId) {
        /*
         * check if collection exists, return dto
         */
        return null;
    }

    public List<CollectionDto> getCollectionsByCreatorId(Integer creatorId) {
        /*
         * check if user exists, return all collections by creator
         */
        return null;
    }

    public List<CollectionDto> getCollections() {
        /*
         * return collections, all or random ??
         */
        return null;
    }

    public void deleteCollection(Integer userId, String collectionId) {
        /*
         * check if user exists, check if creator and user are same, delete
         * collection, delete collection-artwork relations
         */
    }

    private CollectionDto getCollectionDto(Integer collectionId) {

        return null;
    }

}
