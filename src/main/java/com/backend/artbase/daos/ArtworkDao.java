package com.backend.artbase.daos;

import com.backend.artbase.dtos.artwork.ArtworkDto;
import com.backend.artbase.entities.ArtworkStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import com.backend.artbase.core.CustomJdbcTemplate;
import com.backend.artbase.core.CustomSqlParameters;
import com.backend.artbase.core.ResultSetWrapper;
import com.backend.artbase.entities.Artwork;
import com.backend.artbase.entities.ArtworkFilters;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class ArtworkDao {

    private final CustomJdbcTemplate jdbcTemplate;

    public void saveArtwork(Artwork artwork) {
        String artworkStatus = artwork.getArtworkStatus().getCode();
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("artwork_name", artwork.getArtworkName());
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
        params.put("artwork_status", artworkStatus);

        //@formatter:off
        String sql =
            "INSERT INTO artwork (artwork_name, user_id, artist_id, artwork_id, fixed_price, artwork_type_id, " +
            "time_period, rarity_id, medium_id, size_x, size_y, size_z, material_id, " +
            "artwork_location, art_movement_id, acquisition_way, artwork_description, artwork_status) " +
            "VALUES (:user_id, :artist_id, :artwork_id, :fixed_price, :artwork_type_id, " +
            ":time_period, :rarity_id, :medium_id, :size_x, :size_y, :size_z, :material_id, " +
            ":artwork_location, :art_movement_id, :acquisition_way, :artwork_description, :artwork_status)";
        //@formatter:on

        jdbcTemplate.update(sql, params);
    }

    public void updateArtwork(Artwork artwork) {
        String artworkStatus = artwork.getArtworkStatus().getCode();
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("artwork_name", artwork.getArtworkName());
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
        params.put("artwork_status", artworkStatus);

        //@formatter:off
        String sql =
            "UPDATE artwork " +
            "SET artwork_name = :artwork_name, fixed_price = :fixed_price, artwork_type_id = :artwork_type_id, " +
            "time_period = :time_period, rarity_id = :rarity_id, medium_id = :medium_id, " +
            "size_x = :size_x, size_y = :size_y, size_z = :size_z, material_id = :material_id, " +
            "artwork_location = :artwork_location, art_movement_id = :art_movement_id, " +
            "acquisition_way = :acquisition_way, artwork_description = :artwork_description, artwork_status = :artwork_status " +
            "WHERE user_id = :user_id AND artist_id = :artist_id AND artwork_id = :artwork_id";
        //@formatter:on

        jdbcTemplate.update(sql, params);
    }

    public Optional<Artwork> getByArtworkId(Integer artworkId) {
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("artwork_id", artworkId);

        //@formatter:off
        String sql =
            "SELECT a.artwork_name, a.user_id, a.artist_id, a.artwork_id, a.fixed_price, a.artwork_type_id, " +
            "a.time_period, a.rarity_id, a.medium_id, a.size_x, a.size_y, a.size_z, " +
            "a.material_id, a.artwork_location, a.art_movement_id, a.acquisition_way, " +
            "a.artwork_description, a.artwork_status " +
            "FROM artwork a WHERE a.artwork_id = :artwork_id";
        //@formatter:on

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, params, (rs, rnum) -> {
                ResultSetWrapper rsw = new ResultSetWrapper(rs);

                //@formatter:off
                return Artwork.builder()
                    .artworkName(rsw.getString("artwork_name"))
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
                    .artworkStatus(ArtworkStatus.fromCode(rsw.getString("artwork_status")))
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
            "SELECT a.artwork_name, a.user_id, a.artist_id, a.artwork_id, a.fixed_price, a.artwork_type_id, " +
            "a.time_period, a.rarity_id, a.medium_id, a.size_x, a.size_y, a.size_z, " +
            "a.material_id, a.artwork_location, a.art_movement_id, a.acquisition_way, " +
            "a.artwork_description, a.artwork_status " +
            "FROM artwork a WHERE a.artist_id = :artist_id";
        //@formatter:on

        return jdbcTemplate.query(sql, params, (rs, rnum) -> {
            ResultSetWrapper rsw = new ResultSetWrapper(rs);

            //@formatter:off
            return Artwork.builder()
                .artworkName(rsw.getString("artwork_name"))
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
                .artworkStatus(ArtworkStatus.fromCode(rsw.getString("artwork_status")))
                .build();
            //@formatter:on
        });
    }

    public List<Artwork> getArtworkWithFilters(ArtworkFilters filters) {
        CustomSqlParameters params = CustomSqlParameters.create();

        CustomSqlParameters.addFilterParams(params, "medium_ids", filters.getMediumIds());
        CustomSqlParameters.addFilterParams(params, "material_ids", filters.getMaterialIds());
        CustomSqlParameters.addFilterParams(params, "rarity_ids", filters.getRarityIds());
        CustomSqlParameters.addFilterParams(params, "artwork_type_ids", filters.getArtworkTypeIds());

        //@formatter:off
        String sql =
            "SELECT a.artwork_name, a.user_id, a.artist_id, a.artwork_id, a.fixed_price, a.artwork_type_id, " +
            "a.time_period, a.rarity_id, a.medium_id, a.size_x, a.size_y, a.size_z, " +
            "a.material_id, a.artwork_location, a.art_movement_id, a.acquisition_way, " +
            "a.artwork_description, a.artwork_status " +
            "FROM artwork a " +
            "WHERE (:medium_ids IS NULL OR a.medium_id IN (SELECT * FROM STRING_TO_TABLE(:medium_ids, ','))) " +
            "OR (:material_ids IS NULL OR a.material_id IN (SELECT * FROM STRING_TO_TABLE(:material_ids, ','))) " +
            "OR (:rarity_ids IS NULL OR a.rarity_id IN (SELECT * FROM STRING_TO_TABLE(:rarity_ids, ','))) " +
            "OR (:artwork_type_ids IS NULL OR a.artwork_type_id IN (SELECT * FROM STRING_TO_TABLE(:artwork_type_ids, ','))) " +
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
                .artworkName(rsw.getString("artwork_name"))
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
                .artworkStatus(ArtworkStatus.fromCode(rsw.getString("artwork_status")))
                .build();
            //@formatter:on
        });
    }

    public List<Artwork> getArtworkWithFiltersDefinitive(ArtworkFilters filters) {
        CustomSqlParameters params = CustomSqlParameters.create();

        CustomSqlParameters.addFilterParams(params, "medium_ids", filters.getMediumIds());
        CustomSqlParameters.addFilterParams(params, "material_ids", filters.getMaterialIds());
        CustomSqlParameters.addFilterParams(params, "rarity_ids", filters.getRarityIds());
        CustomSqlParameters.addFilterParams(params, "artwork_type_ids", filters.getArtworkTypeIds());

        //@formatter:off
        String sql =
            "SELECT a.artwork_name, a.user_id, a.artist_id, a.artwork_id, a.fixed_price, a.artwork_type_id, " +
            "a.time_period, a.rarity_id, a.medium_id, a.size_x, a.size_y, a.size_z, " +
            "a.material_id, a.artwork_location, a.art_movement_id, a.acquisition_way, " +
            "a.artwork_description, a.artwork_status " +
            "FROM artwork a " +
            "WHERE (:medium_ids IS NULL OR a.medium_id IN (SELECT * FROM STRING_TO_TABLE(:medium_ids, ','))) " +
            "AND (:material_ids IS NULL OR a.material_id IN (SELECT * FROM STRING_TO_TABLE(:material_ids, ','))) " +
            "AND (:rarity_ids IS NULL OR a.rarity_id IN (SELECT * FROM STRING_TO_TABLE(:rarity_ids, ','))) " +
            "AND (:artwork_type_ids IS NULL OR a.artwork_type_id IN (SELECT * FROM STRING_TO_TABLE(:artwork_type_ids, ','))) ";
        
        //@formatter:on

        return jdbcTemplate.query(sql, params, (rs, rnum) -> {
            ResultSetWrapper rsw = new ResultSetWrapper(rs);

            //@formatter:off
            return Artwork.builder()
                .artworkName(rsw.getString("artwork_name"))
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
                .artworkStatus(ArtworkStatus.fromCode(rsw.getString("artwork_status")))
                .build();
            //@formatter:on
        });
    }

    public List<Artwork> searchByName(String searchKey){

        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("search_key", searchKey);

        //@formatter:off
        String sql =
                "SELECT a.artwork_name, a.user_id, a.artist_id, a.artwork_id, a.fixed_price," +
                        "a.artwork_type_id, a.time_period, a.rarity_id, a.medium_id" +
                        "a.size_x, a.size_y, a.size_z, a.material_id, a.artwork_location" +
                        "a.art_movement_id, a.acquisition_way, a.artwork_description, a.artwork_status" +
                "FROM artwork a" +
                "WHERE a.artwork_name LIKE CONCAT('%',:search_key,'%')";
        //@formatter:on

        return jdbcTemplate.query(sql, params, (rs, rnum) -> {
            ResultSetWrapper rsw = new ResultSetWrapper(rs);

            //@formatter:off
            return Artwork.builder()
                    .artworkName(rsw.getString("artwork_name"))
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
                    .artworkStatus(ArtworkStatus.fromCode(rsw.getString("artwork_status")))
                    .build();
            //@formatter:on
        });
    }

    public List<Artwork> searchByDescription(String searchKey){

        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("search_key", searchKey);

        //@formatter:off
        String sql =
                "SELECT a.artwork_name, a.user_id, a.artist_id, a.artwork_id, a.fixed_price," +
                        "a.artwork_type_id, a.time_period, a.rarity_id, a.medium_id" +
                        "a.size_x, a.size_y, a.size_z, a.material_id, a.artwork_location" +
                        "a.art_movement_id, a.acquisition_way, a.artwork_description, a.artwork_status" +
                        "FROM artwork a" +
                        "WHERE a.artwork_description LIKE CONCAT('%',:search_key,'%')";
        //@formatter:on

        return jdbcTemplate.query(sql, params, (rs, rnum) -> {
            ResultSetWrapper rsw = new ResultSetWrapper(rs);

            //@formatter:off
            return Artwork.builder()
                    .artworkName(rsw.getString("artwork_name"))
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
                    .artworkStatus(ArtworkStatus.fromCode(rsw.getString("artwork_status")))
                    .build();
            //@formatter:on
        });
    }

    public List<Artwork> filterSearchByName(String searchKey, ArtworkFilters artworkFilters){

        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("search_key", searchKey);
        CustomSqlParameters.addFilterParams(params, "mediumIds", artworkFilters.getMediumIds());
        CustomSqlParameters.addFilterParams(params, "materialIds", artworkFilters.getMaterialIds());
        CustomSqlParameters.addFilterParams(params, "rarityIds", artworkFilters.getRarityIds());
        CustomSqlParameters.addFilterParams(params, "artworkTypeIds", artworkFilters.getArtworkTypeIds());

        //@formatter:off
        String sql =
                "WITH temp_table as" +
                        "(SELECT a.artwork_name, a.user_id, a.artist_id, a.artwork_id, a.fixed_price," +
                        "a.artwork_type_id, a.time_period, a.rarity_id, a.medium_id" +
                        "a.size_x, a.size_y, a.size_z, a.material_id, a.artwork_location" +
                        "a.art_movement_id, a.acquisition_way, a.artwork_description, a.artwork_status" +
                        "FROM artwork a" +
                        "WHERE a.artwork_name LIKE CONCAT('%',:search_key,'%'))" +
                        "SELECT a.user_id, a.artist_id, a.artwork_id, a.fixed_price, a.artwork_type_id, " +
                        "a.time_period, a.rarity_id, a.medium_id, a.size_x, a.size_y, a.size_z, " +
                        "a.material_id, a.artwork_location, a.art_movement_id, a.acquisition_way, " +
                        "a.artwork_description, a.artwork_status" +
                        "FROM temp_table a " +
                        "WHERE (:medium_ids IS NULL OR a.medium_id IN (SELECT * FROM STRING_TO_TABLE(:medium_ids, ','))) " +
                        "AND (:material_ids IS NULL OR a.material_id IN (SELECT * FROM STRING_TO_TABLE(:material_ids, ','))) " +
                        "AND (:rarity_ids IS NULL OR a.rarity_id IN (SELECT * FROM STRING_TO_TABLE(:rarity_ids, ','))) " +
                        "AND (:artwork_type_ids IS NULL OR a.artwork_type_id IN (SELECT * FROM STRING_TO_TABLE(:artwork_type_ids, ','))) ";

        //@formatter:on

        return jdbcTemplate.query(sql, params, (rs, rnum) -> {
            ResultSetWrapper rsw = new ResultSetWrapper(rs);

            //@formatter:off
            return Artwork.builder()
                    .artworkName(rsw.getString("artwork_name"))
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
                    .artworkStatus(ArtworkStatus.fromCode(rsw.getString("artwork_status")))
                    .build();
            //@formatter:on
        });
    }

    public List<Artwork> filterSearchByDescription(String searchKey, ArtworkFilters artworkFilters){

        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("search_key", searchKey);
        CustomSqlParameters.addFilterParams(params, "mediumIds", artworkFilters.getMediumIds());
        CustomSqlParameters.addFilterParams(params, "materialIds", artworkFilters.getMaterialIds());
        CustomSqlParameters.addFilterParams(params, "rarityIds", artworkFilters.getRarityIds());
        CustomSqlParameters.addFilterParams(params, "artworkTypeIds", artworkFilters.getArtworkTypeIds());

        //@formatter:off
        String sql =
                "WITH temp_table as" +
                        "(SELECT a.artwork_name, a.user_id, a.artist_id, a.artwork_id, a.fixed_price," +
                        "a.artwork_type_id, a.time_period, a.rarity_id, a.medium_id" +
                        "a.size_x, a.size_y, a.size_z, a.material_id, a.artwork_location" +
                        "a.art_movement_id, a.acquisition_way, a.artwork_description, a.artwork_status" +
                        "FROM artwork a" +
                        "WHERE a.artwork_description LIKE CONCAT('%',:search_key,'%'))" +
                        "SELECT a.user_id, a.artist_id, a.artwork_id, a.fixed_price, a.artwork_type_id, " +
                        "a.time_period, a.rarity_id, a.medium_id, a.size_x, a.size_y, a.size_z, " +
                        "a.material_id, a.artwork_location, a.art_movement_id, a.acquisition_way, " +
                        "a.artwork_description, a.artwork_status" +
                        "FROM temp_table a " +
                        "WHERE (:medium_ids IS NULL OR a.medium_id IN (SELECT * FROM STRING_TO_TABLE(:medium_ids, ','))) " +
                        "AND (:material_ids IS NULL OR a.material_id IN (SELECT * FROM STRING_TO_TABLE(:material_ids, ','))) " +
                        "AND (:rarity_ids IS NULL OR a.rarity_id IN (SELECT * FROM STRING_TO_TABLE(:rarity_ids, ','))) " +
                        "AND (:artwork_type_ids IS NULL OR a.artwork_type_id IN (SELECT * FROM STRING_TO_TABLE(:artwork_type_ids, ','))) ";

        //@formatter:on

        return jdbcTemplate.query(sql, params, (rs, rnum) -> {
            ResultSetWrapper rsw = new ResultSetWrapper(rs);

            //@formatter:off
            return Artwork.builder()
                    .artworkName(rsw.getString("artwork_name"))
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
                    .artworkStatus(ArtworkStatus.fromCode(rsw.getString("artwork_status")))
                    .build();
            //@formatter:on
        });
    }

    public List<String> getArtistNamesOfArtworks(List<Artwork> artworks){

        if(artworks.isEmpty()){
            return null;
        }

        List<String> artistNames = new ArrayList<>();
        for(int i = 0; i < artworks.size(); i++){

            CustomSqlParameters params = CustomSqlParameters.create();
            params.put("artist_id", artworks.get(i).getArtistId());

            //@formatter:off
            String sql =
                    "SELECT a.artist_name" +
                    "FROM artist a" +
                    "WHERE a.artist_id = :artist_id";

            String artistName = jdbcTemplate.queryForObject(sql, params, (rs, rnum) -> {
                ResultSetWrapper rsw = new ResultSetWrapper(rs);
                //@formatter:off
                return rsw.getString("artist_name");
                //@formatter:on
            });
            artistNames.add(artistName);
        }
        return artistNames;
    }

    public Integer getNextArtworkId() {
        String sql = "SELECT nextval('artwork_artwork_id_seq') AS next_artwork_id";

        CustomSqlParameters params = CustomSqlParameters.create();

        return jdbcTemplate.queryForObject(sql, params, (rs, rnum) -> {
            ResultSetWrapper rsw = new ResultSetWrapper(rs);
            return rsw.getInteger("next_artwork_id");
        });
    }


    public ArtworkDto getArtworkDto(Integer artworkId){
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("artwork_id", artworkId);

        //@formatter:off
        String sql =
                   "SELECT a.artwork_name, a.user_id, a.artist_id, a.artwork_id, a.fixed_price, a.artwork_type_id, " +
                "a.time_period, a.rarity_id, a.medium_id, a.size_x, a.size_y, a.size_z, " +
                "a.material_id, a.artwork_location, a.art_movement_id, a.acquisition_way, " +
                "a.artwork_description, a.artwork_status, b.artist_name, b.gender, b.nationality, b.age, b.speciality" +
                "FROM artwork a, artist b " +
                "WHERE a.artwork_id = :artwork_id AND a.artist_id = b.artist_id";
        //@formatter:on
        return jdbcTemplate.queryForObject(sql, params, (rs, rnum) -> {
            ResultSetWrapper rsw = new ResultSetWrapper(rs);

            //@formatter:off
            return ArtworkDto.builder()
                    .artworkName(rsw.getString("artwork_name"))
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
                    .artworkStatus(rsw.getString("artwork_status"))
                    .artistName(rsw.getString("artist_name"))
                    .gender(rsw.getString("gender"))
                    .nationality(rsw.getString("nationality"))
                    .age(rsw.getInteger("age"))
                    .speciality(rsw.getString("speciality"))
                    .build();
            //@formatter:on
        });
    }

    public Boolean isArtworkValid(Integer artworkId) {
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("artwork_id", artworkId);

        //@formatter:off
        String sql =
            "SELECT COUNT(*) AS count" +
            "FROM artwork " +
            "WHERE artwork_id = :artwork_id";
        //@formatter:on

        try {
            Integer count = jdbcTemplate.queryForObject(sql, params, (rs, rnum) -> {
                ResultSetWrapper rsw = new ResultSetWrapper(rs);
                return rsw.getInteger("count");
            });
            return count != null && count > 0;
        } catch (EmptyResultDataAccessException ex) {
            return false;
        }
    }

    // TO DO select * from artworks t where t.artwork_id in
    // StrToTable(:artworks)
    // to reduce db calls
    public List<Artwork> getByArtworksIds(List<Integer> artworkId) {
        return null;
    }


}
