package com.backend.artbase.core;

import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class ResultSetWrapper {

    private final ResultSet resultSet;

    public ResultSetWrapper(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public boolean isNull(String columnLabel) throws SQLException {
        return resultSet.getObject(columnLabel) == null;
    }

    public Long getLong(String columnLabel) throws SQLException {
        return isNull(columnLabel) ? null : resultSet.getLong(columnLabel);
    }

    public Integer getInteger(String columnLabel) throws SQLException {
        return isNull(columnLabel) ? null : resultSet.getInt(columnLabel);
    }

    public Double getDouble(String columnLabel) throws SQLException {
        return isNull(columnLabel) ? null : resultSet.getDouble(columnLabel);
    }

    public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
        return isNull(columnLabel) ? null : resultSet.getBigDecimal(columnLabel);
    }

    public String getString(String columnLabel) throws SQLException {
        if (isNull(columnLabel)) {
            return "";
        }

        if (resultSet.getObject(columnLabel) instanceof Clob) {
            return getClob(columnLabel);
        }

        return resultSet.getString(columnLabel);
    }

    public String getClob(String columnLabel) throws SQLException {
        if (isNull(columnLabel)) {
            return "";
        }

        return clobToString(resultSet.getClob(columnLabel));
    }

    private String clobToString(Clob clob) {
        if (clob == null) {
            return "";
        }

        try {
            return clob.getSubString(1, (int) clob.length());
        } catch (SQLException e) {
            System.out.println("SQL Error while converting clob to string");
            return "";
        }
    }

    public LocalDate getLocalDate(String columnLabel) throws SQLException {
        return isNull(columnLabel) ? null : resultSet.getDate(columnLabel).toLocalDate();
    }

    public LocalDateTime getLocalDateTime(String columnLabel) throws SQLException {
        return isNull(columnLabel) ? null : resultSet.getTimestamp(columnLabel).toLocalDateTime();
    }

    /**
     * Kolon değeri null ise null döner, değil ise getBooleanTF methodu gibi
     * davranır.
     * 
     * @param columnLabel
     * @return
     * @throws SQLException
     */
    public Boolean getBoolean(String columnLabel) throws SQLException {
        return isNull(columnLabel) ? null : "T".equals(resultSet.getString(columnLabel));
    }

    /**
     * Kolon değeri 'T' ise true döner, diğer durumlarda false döner.
     * 
     * @param columnLabel
     * @return
     * @throws SQLException
     */
    public boolean getBooleanTF(String columnLabel) throws SQLException {
        return "T".equals(resultSet.getString(columnLabel));
    }

    /***
     * Kolon değeri sıfırdan büyükse true döner, sıfır ve null ise false döner.
     *
     * @param columnLabel
     * @return
     * @throws SQLException
     */
    public boolean getBoolean1v0(String columnLabel) throws SQLException {
        if (isNull(columnLabel)) {
            return false;
        } else {
            return (resultSet.getInt(columnLabel) > 0);
        }
    }

    public LocalTime getLocalTime(String columnLabel) throws SQLException {
        return isNull(columnLabel) ? null : LocalTime.parse(resultSet.getString(columnLabel));
    }

    public boolean next() throws SQLException {
        return resultSet.next();
    }

    public boolean hasColumn(String columnLabel) throws SQLException {
        try {
            return resultSet.findColumn(columnLabel) > -1;
        } catch (SQLException e) {
            return false;
        }
    }

    public String getColumnName(Integer columnIndex) throws SQLException {

        return resultSet.getMetaData().getColumnLabel(columnIndex);
    }

    public List<String> getStringList(String columnLabel, String splitRegex) throws SQLException {

        return isNull(columnLabel) ? Collections.emptyList() : Arrays.asList(resultSet.getString(columnLabel).split(splitRegex));
    }

    public Character getCharacter(String columnName) throws SQLException {
        String result = resultSet.getString(columnName);
        return (result != null && !result.isEmpty()) ? result.charAt(0) : null;
    }

}
