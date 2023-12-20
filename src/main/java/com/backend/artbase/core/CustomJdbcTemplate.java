package com.backend.artbase.core;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterUtils;
import org.springframework.stereotype.Component;

import com.backend.artbase.errors.BaseRuntimeException;
import com.backend.artbase.errors.DatabaseRuntimeException;

@Component
public class CustomJdbcTemplate {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    /**
     * Standard Query
     * 
     * @param sql
     * @param params
     * @param rowMapper
     * @return Object
     */
    public <T> List<T> query(String sql, CustomSqlParameters params, RowMapper<T> rowMapper) {
        try {
            checkParams(sql, params.getParams());
            return namedParameterJdbcTemplate.query(sql, params.getParams(), rowMapper);
        } catch (RuntimeException e) {
            System.out.println("Query runtime exception. SQL: " + sql);
            throw new BaseRuntimeException("Error: " + e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Should only be used for single objects. If there is more than one object
     * returning from query it would throw an error
     * IncorrectResultSizeDataAccessException.
     *
     * @param sql
     * @param params
     * @param rowMapper
     * @return Object
     */
    public <T> T queryForObject(String sql, CustomSqlParameters params, RowMapper<T> rowMapper) {
        try {
            checkParams(sql, params.getParams());
            return namedParameterJdbcTemplate.queryForObject(sql, params.getParams(), rowMapper);
        } catch (EmptyResultDataAccessException e) {
            System.out.println("Emoty result set for db" + e.getMessage());
            throw e;
        } catch (RuntimeException e) {
            System.out.println("Query for object runtime exception. SQL: " + sql);
            throw new DatabaseRuntimeException("Error: " + e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Can be used for insert, update and delete operations.
     *
     * @param sql
     * @param params
     * @param rowMapper
     * @return Object
     */
    public int update(String sql, CustomSqlParameters params) {
        try {
            checkParams(sql, params.getParams());
            return namedParameterJdbcTemplate.update(sql, params.getParams());
        } catch (RuntimeException e) {
            System.out.println("Query for object runtime exception. SQL: " + sql);
            throw new DatabaseRuntimeException("Error: " + e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void checkParams(String sql, MapSqlParameterSource params) {

        List<SqlParameter> sqlStringParameterList = NamedParameterUtils.buildSqlParameterList(NamedParameterUtils.parseSqlStatement(sql),
                CustomSqlParameters.create().getParams());

        params.getValues().keySet().forEach(k -> {
            if (!sqlStringParameterList.stream().anyMatch(p -> p.getName().equals(k))) {
                throw new InvalidDataAccessApiUsageException(k + ": parameter not found in sql");
            }
        });

        sqlStringParameterList.forEach(p -> {
            if (!params.getValues().keySet().contains(p.getName())) {
                throw new InvalidDataAccessApiUsageException(p.getName() + ": no value provided for parameter in sql.");
            }
        });

    }

}
