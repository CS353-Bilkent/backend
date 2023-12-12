package com.backend.artbase.daos;

import org.springframework.stereotype.Repository;

import com.backend.artbase.core.CustomJdbcTemplate;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CollectionDao {

    private final CustomJdbcTemplate jdbcTemplate;
}
