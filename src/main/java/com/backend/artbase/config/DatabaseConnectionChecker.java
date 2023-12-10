package com.backend.artbase.config;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.backend.artbase.core.CustomJdbcTemplate;
import com.backend.artbase.core.CustomSqlParameters;
import com.backend.artbase.core.ResultSetWrapper;

@Component
public class DatabaseConnectionChecker {

    private final CustomJdbcTemplate jdbcTemplate;

    public DatabaseConnectionChecker(CustomJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void checkDatabaseConnection() {
        try {
            jdbcTemplate.queryForObject("SELECT 1", CustomSqlParameters.create(), (rs, rnum) -> {
                ResultSetWrapper rsw = new ResultSetWrapper(rs);
                return 1;
            });

            System.out.println("Database connection is successful!");
        } catch (Exception e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
        }
    }
}
