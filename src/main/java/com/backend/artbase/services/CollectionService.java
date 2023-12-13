package com.backend.artbase.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.artbase.daos.ArtworkDao;
import com.backend.artbase.daos.CollectionDao;
import com.backend.artbase.dtos.collection.CollectionDto;
import com.backend.artbase.entities.Artwork;
import com.backend.artbase.entities.ArtworkCollection;
import com.backend.artbase.entities.User;
import com.backend.artbase.errors.ArtworkException;
import com.backend.artbase.errors.CollectionRuntimeException;

import lombok.RequiredArgsConstructor;

@Transactional
@Service
@RequiredArgsConstructor
public class CollectionService {

    private final CollectionDao collectionDao;
    private final UserService userService;
    private final ArtworkService artworkService;
    private final ArtistService artistService;

    public Integer createCollection(Integer userId, String collectionName, String collectionDesc) {

        // checks for user
        User user = userService.getUserWithId(userId);
        Integer collectionId = collectionDao.getNextCollectionId();

        collectionDao.createCollection(ArtworkCollection.builder().collectionId(collectionId).collectionName(collectionName)
                .collectionDescription(collectionDesc).creatorId(userId).build());

        return collectionId;
    }

    public void saveArtworksToCollection(Integer userId, List<Integer> artworks, Integer collectionId) {
        User user = userService.getUserWithId(userId);
        ArtworkCollection collection = getCollectionById(collectionId);

        if (collection.getCreatorId() != user.getUserId()) {
            throw new CollectionRuntimeException("Only creator of a collection can make changes!", HttpStatus.FORBIDDEN);
        }

        artworks.forEach(e -> {
            if (!artworkService.isArtworkValid(e)) {
                throw new ArtworkException("Artwork with given ID does not exists, id: " + e, HttpStatus.NOT_FOUND);
            }
        });
        collectionDao.addArtworksToCollection(collectionId, artworks);
    }

    public CollectionDto getCollection(Integer collectionId) {

        return getCollectionDto(getCollectionById(collectionId));
    }

    public List<CollectionDto> getCollectionsByCreatorId(Integer creatorId) {
        User user = userService.getUserWithId(creatorId);

        List<ArtworkCollection> collections = collectionDao.getCollectionsByCreatorId(creatorId);

        List<CollectionDto> collectionDtos = new ArrayList<>();

        collections.forEach(e -> collectionDtos.add(getCollectionDto(e)));

        return collectionDtos;
    }

    public List<CollectionDto> getCollections() {
        /*
         * return collections, all or random ??
         */
        return null;
    }

    public void deleteCollection(Integer userId, Integer collectionId) {
        User user = userService.getUserWithId(userId);
        ArtworkCollection collection = getCollectionById(userId);

        if (user.getUserId() != collection.getCreatorId()) {
            throw new CollectionRuntimeException("Only creator of a collection can make changes!", HttpStatus.FORBIDDEN);
        }

        collectionDao.deleteCollection(collectionId);
    }

    // to reduce null checks
    private ArtworkCollection getCollectionById(Integer collectionId) {
        Optional<ArtworkCollection> collection = collectionDao.getCollection(collectionId);

        if (collection.isEmpty()) {
            throw new CollectionRuntimeException("Collection with given ID could not been found", HttpStatus.NOT_FOUND);
        }

        return collection.get();
    }

    // adding artwork dtos to collection
    private CollectionDto getCollectionDto(ArtworkCollection collection) {

        // TO DO: convert to artwork dto
        List<Artwork> artworks = new ArrayList<>();

        List<Integer> artoworkIds = collectionDao.getArtworksFromCollection(collection.getCollectionId());
        artoworkIds.forEach(e -> {
            artworks.add(artworkService.getArtwork(e));
        });

        return CollectionDto.builder().artworks(artworks).collection(collection).build();
    }

}
