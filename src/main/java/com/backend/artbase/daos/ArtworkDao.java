package com.backend.artbase.daos;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import com.backend.artbase.entities.Artwork;
import com.backend.artbase.entities.ArtworkFilters;
import com.backend.artbase.utils.CustomJdbcTemplate;
import com.backend.artbase.utils.CustomSqlParameters;
import com.backend.artbase.utils.ResultSetWrapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class ArtworkDao {

    private final CustomJdbcTemplate jdbcTemplate;

    public void saveArtwork(Artwork artwork) {
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("user_id", artwork.getUserId());
        params.put("artist_id", artwork.getArtistId());
        params.put("artwork_id", artwork.getArtworkId());
        params.put("fixed_price", artwork.getFixedPrice());
        params.put("artwork_type_id", artwork.getArtworkTypeId());
        params.put("time_period", artwork.getTimePeriod());
        params.put("rarity_id", artwork.getRarityId());
        params.put("medium_id", artwork.getMediumId());
        params.put("size_x", artwork.getSizeX());
        params.put("size_y", artwork.getSizeY());
        params.put("size_z", artwork.getSizeZ());
        params.put("material_id", artwork.getMaterialId());
        params.put("artwork_location", artwork.getArtworkLocation());
        params.put("art_movement_id", artwork.getArtMovementId());
        params.put("acquisition_way", artwork.getAcquisitionWay());
        params.put("artwork_description", artwork.getArtworkDescription());

        //@formatter:off
        String sql =
            "INSERT INTO artwork (user_id, artist_id, artwork_id, fixed_price, artwork_type_id, " +
            "time_period, rarity_id, medium_id, size_x, size_y, size_z, material_id, " +
            "artwork_location, art_movement_id, acquisition_way, artwork_description) " +
            "VALUES (:user_id, :artist_id, :artwork_id, :fixed_price, :artwork_type_id, " +
            ":time_period, :rarity_id, :medium_id, :size_x, :size_y, :size_z, :material_id, " +
            ":artwork_location, :art_movement_id, :acquisition_way, :artwork_description)";
        //@formatter:on

        jdbcTemplate.update(sql, params);
    }

    public void updateArtwork(Artwork artwork) {
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("fixed_price", artwork.getFixedPrice());
        params.put("artwork_type_id", artwork.getArtworkTypeId());
        params.put("time_period", artwork.getTimePeriod());
        params.put("rarity_id", artwork.getRarityId());
        params.put("medium_id", artwork.getMediumId());
        params.put("size_x", artwork.getSizeX());
        params.put("size_y", artwork.getSizeY());
        params.put("size_z", artwork.getSizeZ());
        params.put("material_id", artwork.getMaterialId());
        params.put("artwork_location", artwork.getArtworkLocation());
        params.put("art_movement_id", artwork.getArtMovementId());
        params.put("acquisition_way", artwork.getAcquisitionWay());
        params.put("artwork_description", artwork.getArtworkDescription());
        params.put("user_id", artwork.getUserId());
        params.put("artist_id", artwork.getArtistId());
        params.put("artwork_id", artwork.getArtworkId());

        //@formatter:off
        String sql =
            "UPDATE artwork " +
            "SET fixed_price = :fixed_price, artwork_type_id = :artwork_type_id, " +
            "time_period = :time_period, rarity_id = :rarity_id, medium_id = :medium_id, " +
            "size_x = :size_x, size_y = :size_y, size_z = :size_z, material_id = :material_id, " +
            "artwork_location = :artwork_location, art_movement_id = :art_movement_id, " +
            "acquisition_way = :acquisition_way, artwork_description = :artwork_description " +
            "WHERE user_id = :user_id AND artist_id = :artist_id AND artwork_id = :artwork_id";
        //@formatter:on

        jdbcTemplate.update(sql, params);
    }

    public Optional<Artwork> getByArtworkId(Integer artworkId) {
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("artwork_id", artworkId);

        //@formatter:off
        String sql =
            "SELECT a.user_id, a.artist_id, a.artwork_id, a.fixed_price, a.artwork_type_id, " +
            "a.time_period, a.rarity_id, a.medium_id, a.size_x, a.size_y, a.size_z, " +
            "a.material_id, a.artwork_location, a.art_movement_id, a.acquisition_way, " +
            "a.artwork_description " +
            "FROM artwork a WHERE a.artwork_id = :artwork_id";
        //@formatter:on

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, params, (rs, rnum) -> {
                ResultSetWrapper rsw = new ResultSetWrapper(rs);

                //@formatter:off
                return Artwork.builder()
                    .userId(rsw.getInteger("user_id"))
                    .artistId(rsw.getInteger("artist_id"))
                    .artworkId(rsw.getInteger("artwork_id"))
                    .fixedPrice(rsw.getDouble("fixed_price"))
                    .artworkTypeId(rsw.getInteger("artwork_type_id"))
                    .timePeriod(rsw.getString("time_period"))
                    .rarityId(rsw.getInteger("rarity_id"))
                    .mediumId(rsw.getInteger("medium_id"))
                    .sizeX(rsw.getDouble("size_x"))
                    .sizeY(rsw.getDouble("size_y"))
                    .sizeZ(rsw.getDouble("size_z"))
                    .materialId(rsw.getInteger("material_id"))
                    .artworkLocation(rsw.getString("artwork_location"))
                    .artMovementId(rsw.getInteger("art_movement_id"))
                    .acquisitionWay(rsw.getString("acquisition_way"))
                    .artworkDescription(rsw.getString("artwork_description"))
                    .build();
                //@formatter:on
            }));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    public List<Artwork> getArtworksOfArtist(Integer artistId) {
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("artist_id", artistId);

        //@formatter:off
        String sql =
            "SELECT a.user_id, a.artist_id, a.artwork_id, a.fixed_price, a.artwork_type_id, " +
            "a.time_period, a.rarity_id, a.medium_id, a.size_x, a.size_y, a.size_z, " +
            "a.material_id, a.artwork_location, a.art_movement_id, a.acquisition_way, " +
            "a.artwork_description " +
            "FROM artwork a WHERE a.artist_id = :artist_id";
        //@formatter:on

        return jdbcTemplate.query(sql, params, (rs, rnum) -> {
            ResultSetWrapper rsw = new ResultSetWrapper(rs);

            //@formatter:off
            return Artwork.builder()
                .userId(rsw.getInteger("user_id"))
                .artistId(rsw.getInteger("artist_id"))
                .artworkId(rsw.getInteger("artwork_id"))
                .fixedPrice(rsw.getDouble("fixed_price"))
                .artworkTypeId(rsw.getInteger("artwork_type_id"))
                .timePeriod(rsw.getString("time_period"))
                .rarityId(rsw.getInteger("rarity_id"))
                .mediumId(rsw.getInteger("medium_id"))
                .sizeX(rsw.getDouble("size_x"))
                .sizeY(rsw.getDouble("size_y"))
                .sizeZ(rsw.getDouble("size_z"))
                .materialId(rsw.getInteger("material_id"))
                .artworkLocation(rsw.getString("artwork_location"))
                .artMovementId(rsw.getInteger("art_movement_id"))
                .acquisitionWay(rsw.getString("acquisition_way"))
                .artworkDescription(rsw.getString("artwork_description"))
                .build();
            //@formatter:on
        });
    }

    public List<Artwork> getArtworkWithFilters(ArtworkFilters filters) {
        CustomSqlParameters params = CustomSqlParameters.create();

        addFilterParams(params, "medium_ids", filters.getMediumIds());
        addFilterParams(params, "material_ids", filters.getMaterialIds());
        addFilterParams(params, "rarity_ids", filters.getRarityIds());
        addFilterParams(params, "artwork_type_ids", filters.getArtworkTypeIds());

        //@formatter:off
        String sql =
            "SELECT a.user_id, a.artist_id, a.artwork_id, a.fixed_price, a.artwork_type_id, " +
            "a.time_period, a.rarity_id, a.medium_id, a.size_x, a.size_y, a.size_z, " +
            "a.material_id, a.artwork_location, a.art_movement_id, a.acquisition_way, " +
            "a.artwork_description " +
            "FROM artwork a " +
            "WHERE (:medium_ids IS NULL OR a.medium_id IN (SELECT * FROM STRING_TO_TABLE(:medium_ids, ','))) " +
            "AND (:material_ids IS NULL OR a.material_id IN (SELECT * FROM STRING_TO_TABLE(:material_ids, ','))) " +
            "AND (:rarity_ids IS NULL OR a.rarity_id IN (SELECT * FROM STRING_TO_TABLE(:rarity_ids, ','))) " +
            "AND (:artwork_type_ids IS NULL OR a.artwork_type_id IN (SELECT * FROM STRING_TO_TABLE(:artwork_type_ids, ','))) " +
            "ORDER BY " +
            "CASE WHEN (:medium_ids IS NOT NULL AND a.medium_id IN (SELECT * FROM STRING_TO_TABLE(:medium_ids, ','))) THEN 1 ELSE 0 END + " +
            "CASE WHEN (:material_ids IS NOT NULL AND a.material_id IN (SELECT * FROM STRING_TO_TABLE(:material_ids, ','))) THEN 1 ELSE 0 END + " +
            "CASE WHEN (:rarity_ids IS NOT NULL AND a.rarity_id IN (SELECT * FROM STRING_TO_TABLE(:rarity_ids, ','))) THEN 1 ELSE 0 END + " +
            "CASE WHEN (:artwork_type_ids IS NOT NULL AND a.artwork_type_id IN (SELECT * FROM STRING_TO_TABLE(:artwork_type_ids, ','))) THEN 1 ELSE 0 END " +
            "DESC";
        //@formatter:on

        return jdbcTemplate.query(sql, params, (rs, rnum) -> {
            ResultSetWrapper rsw = new ResultSetWrapper(rs);

            //@formatter:off
            return Artwork.builder()
                .userId(rsw.getInteger("user_id"))
                .artistId(rsw.getInteger("artist_id"))
                .artworkId(rsw.getInteger("artwork_id"))
                .fixedPrice(rsw.getDouble("fixed_price"))
                .artworkTypeId(rsw.getInteger("artwork_type_id"))
                .timePeriod(rsw.getString("time_period"))
                .rarityId(rsw.getInteger("rarity_id"))
                .mediumId(rsw.getInteger("medium_id"))
                .sizeX(rsw.getDouble("size_x"))
                .sizeY(rsw.getDouble("size_y"))
                .sizeZ(rsw.getDouble("size_z"))
                .materialId(rsw.getInteger("material_id"))
                .artworkLocation(rsw.getString("artwork_location"))
                .artMovementId(rsw.getInteger("art_movement_id"))
                .acquisitionWay(rsw.getString("acquisition_way"))
                .artworkDescription(rsw.getString("artwork_description"))
                .build();
            //@formatter:on
        });
    }

    // Modified helper method to add filter parameters as comma-separated
    // strings
    private void addFilterParams(CustomSqlParameters params, String paramName, List<Integer> paramValues) {
        if (paramValues != null && !paramValues.isEmpty()) {
            String paramValueString = paramValues.stream().map(String::valueOf).collect(Collectors.joining(","));
            params.put(paramName, paramValueString);
        } else {
            params.put(paramName, "");
        }
    }
}