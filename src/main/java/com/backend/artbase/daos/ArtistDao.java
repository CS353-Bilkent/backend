package com.backend.artbase.daos;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import com.backend.artbase.core.CustomJdbcTemplate;
import com.backend.artbase.core.CustomSqlParameters;
import com.backend.artbase.core.ResultSetWrapper;
import com.backend.artbase.entities.Artist;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class ArtistDao {

    private final CustomJdbcTemplate jdbcTemplate;

    public void saveArtist(Artist artist) {
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("user_id", artist.getUserId());
        params.put("artist_id", artist.getArtistId());
        params.put("artist_name", artist.getArtistName());
        params.put("gender", artist.getGender());
        params.put("nationality", artist.getNationality());
        params.put("age", artist.getAge());
        params.put("speciality", artist.getSpeciality());

        //@formatter:off
        String sql =
            "INSERT INTO artist (user_id, artist_id, artist_name, gender, nationality, age, speciality) " +
            "VALUES (:user_id, :artist_id, :artist_name, :gender, :nationality, :age, :speciality)";
        //@formatter:on

        jdbcTemplate.update(sql, params);
    }

    public void updateArtist(Artist artist) {
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("artist_name", artist.getArtistName());
        params.put("gender", artist.getGender());
        params.put("nationality", artist.getNationality());
        params.put("age", artist.getAge());
        params.put("speciality", artist.getSpeciality());
        params.put("user_id", artist.getUserId());

        //@formatter:off
        String sql =
            "UPDATE artist " +
            "SET artist_name = :artist_name, gender = :gender, nationality = :nationality, " +
            "age = :age, speciality = :speciality " +
            "WHERE user_id = :user_id";
        //@formatter:on

        jdbcTemplate.update(sql, params);
    }

    public Optional<Artist> getByArtistId(Integer artistId) {
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("artist_id", artistId);

        //@formatter:off
        String sql =
            "SELECT a.user_id, a.artist_id, a.artist_name, a.gender, a.nationality, a.age, a.speciality " +
            "FROM artist a WHERE a.artist_id = :artist_id";
        //@formatter:on

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, params, (rs, rnum) -> {
                ResultSetWrapper rsw = new ResultSetWrapper(rs);

                //@formatter:off
                return Artist.builder()
                    .userId(rsw.getInteger("user_id"))
                    .artistId(rsw.getInteger("artist_id"))
                    .artistName(rsw.getString("artist_name"))
                    .gender(rsw.getString("gender"))
                    .nationality(rsw.getString("nationality"))
                    .age(rsw.getInteger("age"))
                    .speciality(rsw.getString("speciality"))
                    .build();
                //@formatter:on
            }));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    public Optional<Artist> getByUserId(Integer userId) {
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("user_id", userId);

        //@formatter:off
        String sql =
            "SELECT a.user_id, a.artist_id, a.artist_name, a.gender, a.nationality, a.age, a.speciality " +
            "FROM artist a WHERE a.user_id = :user_id";
        //@formatter:on

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, params, (rs, rnum) -> {
                ResultSetWrapper rsw = new ResultSetWrapper(rs);

                //@formatter:off
                return Artist.builder()
                    .userId(rsw.getInteger("user_id"))
                    .artistId(rsw.getInteger("artist_id"))
                    .artistName(rsw.getString("artist_name"))
                    .gender(rsw.getString("gender"))
                    .nationality(rsw.getString("nationality"))
                    .age(rsw.getInteger("age"))
                    .speciality(rsw.getString("speciality"))
                    .build();
                //@formatter:on
            }));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    public Integer getNextArtistId() {
        String sql = "SELECT nextval('artist_artist_id_seq') AS next_artist_id";

        CustomSqlParameters params = CustomSqlParameters.create();

        return jdbcTemplate.queryForObject(sql, params, (rs, rnum) -> {
            ResultSetWrapper rsw = new ResultSetWrapper(rs);
            return rsw.getInteger("next_artist_id");
        });
    }
}
