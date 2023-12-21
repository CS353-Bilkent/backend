package com.backend.artbase.daos;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.backend.artbase.dtos.workshop.WorkshopDto;

import java.util.List;

@Repository
public class WorkshopDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public WorkshopDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<WorkshopDto> findAll() {
        String sql = "SELECT * FROM workshops";
        return jdbcTemplate.query(sql, (rs, rowNum) -> WorkshopDto.builder()
                .workshopId(rs.getInt("workshop_id"))
                .artistId(rs.getInt("artist_id"))
                .workshopDescription(rs.getString("workshop_description"))
                .dateTime(rs.getTimestamp("date_time").toLocalDateTime())
                .duration(rs.getInt("duration"))
                .mediumId(rs.getInt("medium_id"))
                .price(rs.getBigDecimal("price"))
                .capacity(rs.getInt("capacity"))
                .title(rs.getString("title"))
                .workshopType(rs.getString("workshop_type"))
                .build()
        );
    }
    
}
