package com.backend.artbase.core;

import java.math.BigDecimal;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterUtils;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.jdbc.support.lob.DefaultLobHandler;

import com.backend.artbase.utils.JdbcUtils;

public final class CustomSqlParameters {
    private final MapSqlParameterSource params = new MapSqlParameterSource();

    private CustomSqlParameters() {
    }

    public static CustomSqlParameters create() {
        return new CustomSqlParameters();
    }

    public MapSqlParameterSource getParams() {
        return params;
    }

    public void put(String name, Integer value) {
        params.addValue(name, value);
    }

    public void put(String name, Long value) {
        params.addValue(name, value);
    }

    public void put(String name, Double value) {
        params.addValue(name, value);
    }

    public void put(String name, BigDecimal value) {
        params.addValue(name, value);
    }

    public void put(String name, Float value) {
        params.addValue(name, value);
    }

    public void put(String name, Date value) {
        params.addValue(name, JdbcUtils.convert(value));
    }

    public void put(String name, LocalDateTime value) {
        params.addValue(name, JdbcUtils.convert(value));
    }

    public void put(String name, LocalDate value) {
        params.addValue(name, JdbcUtils.convert(value));
    }

    public void put(String name, Boolean value) {
        if (value != null) {
            params.addValue(name, value);
        } else {
            putNull(name);
        }
    }

    public void put(String name, String value) {
        if (value == null) {
            putNull(name);
        } else if (value.length() > 4000) {
            DefaultLobHandler lobHandler = new DefaultLobHandler();
            lobHandler.setStreamAsLob(true);
            params.addValue(name, new SqlLobValue(value, lobHandler), Types.CLOB);
        } else {
            params.addValue(name, value);
        }
    }

    public void putListOfLong(String name, List<Long> value) {
        put(name, StringUtils.join(value, ","));
    }

    public void putListOfInteger(String name, List<Integer> value) {
        put(name, StringUtils.join(value, ","));
    }

    public void putListOfString(String name, List<String> value) {
        put(name, StringUtils.join(value, ","));
    }

    public void put(String name, byte[] value) {
        DefaultLobHandler lobHandler = new DefaultLobHandler();
        lobHandler.setStreamAsLob(true);
        params.addValue(name, new SqlLobValue(value, lobHandler), Types.BLOB);
    }

    public void put(String name, Object value, Integer sqlType) {
        params.addValue(name, value, sqlType);
    }

    public void putNull(String name) {
        params.addValue(name, null);
    }

    public void addMissingParamswithNull(String sql) {
        List<SqlParameter> sqlStringParameterList = NamedParameterUtils.buildSqlParameterList(NamedParameterUtils.parseSqlStatement(sql),
                CustomSqlParameters.create().getParams());

        sqlStringParameterList.forEach(p -> {
            if (!params.getValues().keySet().contains(p.getName())) {
                putNull(p.getName());
            }
        });
    }

    public void checkParams(String sql) {
        this.checkParams(sql, params);
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

    public static void addFilterParams(CustomSqlParameters params, String paramName, List<Integer> paramValues) {
        if (paramValues != null && !paramValues.isEmpty()) {
            String paramValueString = paramValues.stream().map(String::valueOf).collect(Collectors.joining(","));
            params.put(paramName, paramValueString);
        } else {
            params.put(paramName, "");
        }
    }

}
