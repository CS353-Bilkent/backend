package com.backend.artbase.utils;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomJdbcTemplate {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    /**
     * Sorgu sonucunda tek satır bekleniyorsa kullanılmalıdır. Hiç satır
     * dönmezse null döner Birden fazla satır dönerse
     * IncorrectResultSizeDataAccessException patlatır.
     *
     * @param sql
     * @param params
     * @param rowMapper
     * @return Object
     */
    public <T> T queryForObject(String sql, CustomSqlParameters params, RowMapper<T> rowMapper) {
        try {
            return namedParameterJdbcTemplate.queryForObject(sql, params.getParams(), rowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (RuntimeException e) {
            System.out.println("Query for object runtime exception. SQL: " + sql);
            throw e;
        }
    }

}
