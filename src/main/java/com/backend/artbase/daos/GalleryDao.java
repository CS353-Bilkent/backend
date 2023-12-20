package com.backend.artbase.daos;

import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import com.backend.artbase.core.CustomJdbcTemplate;
import com.backend.artbase.core.CustomSqlParameters;
import com.backend.artbase.core.ResultSetWrapper;
import com.backend.artbase.entities.ArtGallery;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class GalleryDao {

    private final CustomJdbcTemplate jdbcTemplate;

    public Optional<ArtGallery> getByUserId(Integer userId) {

        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("user_id", userId);

        //@formatter:off
        String sql = 
            "SELECT art_gallery_id, user_id, art_gallery_name, art_gallery_location " 
            + "FROM art_gallery "
            + "WHERE user_id = :user_id";
        //@formatter:on

        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, params, (rs, rnum) -> {
                ResultSetWrapper rsw = new ResultSetWrapper(rs);
                //@formatter:off
                return ArtGallery.builder()
                    .artGalleryId(rsw.getInteger("art_gallery_id"))
                    .artGalleryLocation(rsw.getString("art_gallery_location"))
                    .artGalleryName(rsw.getString("art_gallery_name"))
                    .userId(rsw.getInteger("user_id"))
                .build();
                //@formatter:on
            }));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

    }

    public Integer getNextGalleryId() {
        String sql = "SELECT nextval('art_gallery_art_gallery_id_seq') AS next_gallery_id";

        CustomSqlParameters params = CustomSqlParameters.create();

        return jdbcTemplate.queryForObject(sql, params, (rs, rnum) -> {
            ResultSetWrapper rsw = new ResultSetWrapper(rs);
            return rsw.getInteger("next_gallery_id");
        });
    }

    public void saveGallery(ArtGallery gallery) {

        //@formatter:off
        String sql =
                  "INSERT INTO art_gallery (art_gallery_id, user_id, art_gallery_name, art_gallery_location) "
                + "VALUES (:art_gallery_id, :user_id, :art_gallery_name, :art_gallery_location)";
        //@formatter:on

        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("art_gallery_id", gallery.getArtGalleryId());
        params.put("user_id", gallery.getUserId());
        params.put("art_gallery_name", gallery.getArtGalleryName());
        params.put("art_gallery_location", gallery.getArtGalleryLocation());

        jdbcTemplate.update(sql, params);

    }

}
