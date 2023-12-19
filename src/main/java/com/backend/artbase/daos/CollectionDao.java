package com.backend.artbase.daos;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.backend.artbase.core.CustomJdbcTemplate;
import com.backend.artbase.core.CustomSqlParameters;
import com.backend.artbase.core.ResultSetWrapper;
import com.backend.artbase.entities.ArtworkCollection;
import com.backend.artbase.errors.CollectionRuntimeException;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CollectionDao {

    private final CustomJdbcTemplate jdbcTemplate;

    public Optional<ArtworkCollection> getCollection(Integer collectionId) {
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("collection_id", collectionId);

        //@formatter:off
        String collectionSql =
            "SELECT ac.artwork_collection_id, ac.artwork_collection_name, ac.artwork_collection_description, ac.creator_id " +
            "FROM artwork_collection ac WHERE ac.artwork_collection_id = :collection_id";
        //@formatter:on

        try {
            // Retrieve collection information
            ArtworkCollection artworkCollection = jdbcTemplate.queryForObject(collectionSql, params, (rs, rnum) -> {
                ResultSetWrapper rsw = new ResultSetWrapper(rs);
                //@formatter:off
                return ArtworkCollection.builder()
                    .collectionId(rsw.getInteger("artwork_collection_id"))
                    .collectionName(rsw.getString("artwork_collection_name"))
                    .collectionDescription(rsw.getString("artwork_collection_description"))
                    .creatorId(rsw.getInteger("creator_id"))
                    .build();
                //@formatter:on
            });

            return Optional.of(artworkCollection);
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    public List<ArtworkCollection> getAllCollections() {
        CustomSqlParameters params = CustomSqlParameters.create();

        //@formatter:off
        String collectionSql =
            "SELECT ac.artwork_collection_id, ac.artwork_collection_name, ac.artwork_collection_description, ac.creator_id " +
            "FROM artwork_collection ac ";
        //@formatter:on

        // Retrieve collection information
        return jdbcTemplate.query(collectionSql, params, (rs, rnum) -> {
            ResultSetWrapper rsw = new ResultSetWrapper(rs);
            //@formatter:off
                return ArtworkCollection.builder()
                    .collectionId(rsw.getInteger("artwork_collection_id"))
                    .collectionName(rsw.getString("artwork_collection_name"))
                    .collectionDescription(rsw.getString("artwork_collection_description"))
                    .creatorId(rsw.getInteger("creator_id"))
                    .build();
                //@formatter:on
        });

    }

    public Integer getNextCollectionId() {
        String sql = "SELECT nextval('artwork_collection_artwork_collection_id_seq') AS next_collection_id";

        CustomSqlParameters params = CustomSqlParameters.create();

        return jdbcTemplate.queryForObject(sql, params, (rs, rnum) -> {
            ResultSetWrapper rsw = new ResultSetWrapper(rs);
            return rsw.getInteger("next_collection_id");
        });
    }

    public void createCollection(ArtworkCollection artworkCollection) {
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("artwork_collection_id", artworkCollection.getCollectionId());
        params.put("collection_name", artworkCollection.getCollectionName());
        params.put("collection_description", artworkCollection.getCollectionDescription());
        params.put("creator_id", artworkCollection.getCreatorId());

        //@formatter:off
        String sql =
            "INSERT INTO artwork_collection " +
            "(artwork_collection_id, artwork_collection_name, artwork_collection_description, creator_id) " +
            "VALUES (:artwork_collection_id, :collection_name, :collection_description, :creator_id)";
        //@formatter:on

        try {
            jdbcTemplate.update(sql, params);
        } catch (DataAccessException ex) {
            throw new CollectionRuntimeException("Error creating collection " + artworkCollection.getCollectionName(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<ArtworkCollection> getCollectionsByCreatorId(Integer creatorId) {
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("creator_id", creatorId);

        //@formatter:off
        String sql =
            "SELECT ac.artwork_collection_id, ac.artwork_collection_name, ac.artwork_collection_description, ac.creator_id " +
            "FROM artwork_collection ac WHERE ac.creator_id = :creator_id";
        //@formatter:on

        return jdbcTemplate.query(sql, params, (rs, rnum) -> {
            ResultSetWrapper rsw = new ResultSetWrapper(rs);

            //@formatter:off
            return ArtworkCollection.builder()
                    .collectionId(rsw.getInteger("artwork_collection_id"))
                    .collectionName(rsw.getString("artwork_collection_name"))
                    .collectionDescription(rsw.getString("artwork_collection_description"))
                    .creatorId(rsw.getInteger("creator_id"))
                    .build();
            //@formatter:on
        });
    }

    public List<Integer> getArtworksFromCollection(Integer collectionId) {
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("collection_id", collectionId);

        //@formatter:off
        String sql =
            "SELECT ca.artwork_id " +
            "FROM collection_to_artwork ca WHERE ca.artwork_collection_id = :collection_id";
        //@formatter:on

        return jdbcTemplate.query(sql, params, (rs, rnum) -> {
            ResultSetWrapper rsw = new ResultSetWrapper(rs);
            return rsw.getInteger("artwork_id");
        });
    }

    public void deleteCollection(Integer collectionId) {
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("collection_id", collectionId);

        //@formatter:off
        String deleteCollectionSql =
            "DELETE FROM artwork_collection WHERE artwork_collection_id = :collection_id";
        
        String deleteArtworksSql =
            "DELETE FROM collection_to_artwork WHERE artwork_collection_id = :collection_id";
        //@formatter:on

        try {
            jdbcTemplate.update(deleteArtworksSql, params);
        } catch (DataAccessException ex) {
            throw new CollectionRuntimeException("Error deleting collection: " + collectionId, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            jdbcTemplate.update(deleteCollectionSql, params);
        } catch (DataAccessException ex) {
            throw new CollectionRuntimeException("Error deleting artworks relations to collection : " + collectionId,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void addArtworksToCollection(Integer collectionId, List<Integer> newArtworkIds) {

        try {
            for (Integer artworkId : newArtworkIds) {
                CustomSqlParameters params = CustomSqlParameters.create();
                params.put("collection_id", collectionId);
                params.put("artwork_id", artworkId);

                //@formatter:off
                String insertSql =
                    "INSERT INTO collection_to_artwork (artwork_collection_id, artwork_id) " +
                    "VALUES (:collection_id, :artwork_id)";
                //@formatter:on

                jdbcTemplate.update(insertSql, params);
            }
        } catch (DataAccessException ex) {
            throw new CollectionRuntimeException("Error adding artworks to collection", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
