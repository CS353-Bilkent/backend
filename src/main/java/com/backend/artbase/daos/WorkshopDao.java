package com.backend.artbase.daos;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.backend.artbase.core.CustomJdbcTemplate;
import com.backend.artbase.core.CustomSqlParameters;
import com.backend.artbase.core.ResultSetWrapper;
import com.backend.artbase.dtos.workshop.WorkshopDto;
import com.backend.artbase.entities.Workshop;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class WorkshopDao {

    private final CustomJdbcTemplate jdbcTemplate;

    public List<WorkshopDto> findAll() {
        String sql = "SELECT * FROM workshop";
        CustomSqlParameters params = CustomSqlParameters.create();

        return jdbcTemplate.query(sql, params, (rs, rnum) -> {

            ResultSetWrapper rsw = new ResultSetWrapper(rs);

            return WorkshopDto.builder().workshopId(rsw.getInteger("workshop_id")).artistId(rsw.getInteger("artist_id"))
                    .workshopDescription(rsw.getString("workshop_description")).dateTime(rs.getTimestamp("date_time").toLocalDateTime())
                    .duration(rsw.getInteger("duration")).mediumId(rsw.getInteger("medium_id")).price(rsw.getBigDecimal("price"))
                    .capacity(rsw.getInteger("capacity")).title(rsw.getString("title")).workshopType(rsw.getString("workshop_type"))
                    .build();
        });
    }

    public void saveWorkshop(Workshop workshop) {

        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("workshop_id", workshop.getWorkshopId());
        params.put("artist_id", workshop.getArtistId());
        params.put("workshop_description", workshop.getWorkshopDescription());
        params.put("date_time", workshop.getDateTime());
        params.put("duration", workshop.getDuration());
        params.put("medium_id", workshop.getMediumId());
        params.put("price", workshop.getPrice());
        params.put("capacity", workshop.getCapacity());
        params.put("title", workshop.getTitle());
        params.put("workshop_type", workshop.getWorkshopType());

        //@formatter:off
        String sql =
                "INSERT INTO public.workshop(" +
                        "workshop_id, artist_id, workshop_description, date_time, duration, medium_id, " +
                        "price, capacity, title, workshop_type) " +
                        "VALUES (:workshop_id, :artist_id, :workshop_description, :date_time, :duration, " +
                        ":medium_id, :price, :capacity, :title, :workshop_type)";
        //@formatter:on

        jdbcTemplate.update(sql, params);
    }

    public List<WorkshopDto> findByArtistId(Integer artistId) {
        String sql = "SELECT workshop_id, artist_id, workshop_description, date_time, duration, medium_id, "
                + "price, capacity, title, workshop_type FROM workshop WHERE artist_id = :artist_id";

        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("artist_id", artistId);

        return jdbcTemplate.query(sql, params, (rs, rnum) -> {
            ResultSetWrapper rsw = new ResultSetWrapper(rs);

            return WorkshopDto.builder().workshopId(rsw.getInteger("workshop_id")).artistId(rsw.getInteger("artist_id"))
                    .workshopDescription(rsw.getString("workshop_description")).dateTime(rs.getTimestamp("date_time").toLocalDateTime())
                    .duration(rsw.getInteger("duration")).mediumId(rsw.getInteger("medium_id")).price(rsw.getBigDecimal("price"))
                    .capacity(rsw.getInteger("capacity")).title(rsw.getString("title")).workshopType(rsw.getString("workshop_type"))
                    .build();
        });
    }

    public Integer getNextWorkshopId() {
        String sql = "SELECT nextval('workshop_workshop_id_seq') AS next_workshop_id";

        CustomSqlParameters params = CustomSqlParameters.create();

        return jdbcTemplate.queryForObject(sql, params, (rs, rnum) -> {
            ResultSetWrapper rsw = new ResultSetWrapper(rs);
            return rsw.getInteger("next_workshop_id");
        });
    }

    public void participateToWorkshop(Integer userId, Integer workshopId) {
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("user_id", userId);
        params.put("workshop_id", workshopId);

    //@formatter:off
    String sql = "INSERT INTO workshop_participation (user_id, workshop_id) " +
                 "VALUES (:user_id, :workshop_id)";
    //@formatter:on

        jdbcTemplate.update(sql, params);
    }

    public List<WorkshopDto> getParticipatedWorkshops(Integer userId) {
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("user_id", userId);

        //@formatter:off
        String sql = "SELECT w.workshop_id, w.artist_id, w.workshop_description, w.date_time, " +
                    "w.duration, w.medium_id, w.price, w.capacity, w.title, w.workshop_type " +
                    "FROM workshop w " +
                    "INNER JOIN workshop_participation wp ON w.workshop_id = wp.workshop_id " +
                    "WHERE wp.user_id = :user_id";
        //@formatter:on

        return jdbcTemplate.query(sql, params, (rs, rnum) -> {
            ResultSetWrapper rsw = new ResultSetWrapper(rs);

            return WorkshopDto.builder().workshopId(rsw.getInteger("workshop_id")).artistId(rsw.getInteger("artist_id"))
                    .workshopDescription(rsw.getString("workshop_description")).dateTime(rs.getTimestamp("date_time").toLocalDateTime())
                    .duration(rsw.getInteger("duration")).mediumId(rsw.getInteger("medium_id")).price(rsw.getBigDecimal("price"))
                    .capacity(rsw.getInteger("capacity")).title(rsw.getString("title")).workshopType(rsw.getString("workshop_type"))
                    .build();
        });
    }

}
