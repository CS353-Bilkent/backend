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

import org.springframework.http.HttpStatus;

import com.backend.artbase.errors.DatabaseRuntimeException;

public final class ResultSetWrapper {

    private final ResultSet resultSet;

    public ResultSetWrapper(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public boolean isNull(String columnName) {
        try {
            return resultSet.getObject(columnName) == null;
        } catch (SQLException e) {
            throw new DatabaseRuntimeException("Column with given name is not found in SQL query: " + columnName, HttpStatus.NOT_FOUND);

        }
    }

    public Long getLong(String columnName) {
        try {
            return isNull(columnName) ? null : resultSet.getLong(columnName);
        } catch (SQLException e) {
            throw new DatabaseRuntimeException("Column with given name is not found in SQL query: " + columnName, HttpStatus.NOT_FOUND);

        }
    }

    public Integer getInteger(String columnName) {
        try {
            return isNull(columnName) ? null : resultSet.getInt(columnName);
        } catch (SQLException e) {
            throw new DatabaseRuntimeException("Column with given name is not found in SQL query: " + columnName, HttpStatus.NOT_FOUND);

        }
    }

    public Double getDouble(String columnName) {

        try {
            return isNull(columnName) ? null : resultSet.getDouble(columnName);
        } catch (SQLException e) {
            throw new DatabaseRuntimeException("Column with given name is not found in SQL query: " + columnName, HttpStatus.NOT_FOUND);

        }
    }

    public BigDecimal getBigDecimal(String columnName) {
        try {
            return isNull(columnName) ? null : resultSet.getBigDecimal(columnName);
        } catch (SQLException e) {
            throw new DatabaseRuntimeException("Column with given name is not found in SQL query: " + columnName, HttpStatus.NOT_FOUND);

        }
    }

    public String getString(String columnName) {
        try {

            if (isNull(columnName)) {
                return "";
            }

            if (resultSet.getObject(columnName) instanceof Clob) {
                return getClob(columnName);
            }

            return resultSet.getString(columnName);
        } catch (SQLException e) {
            throw new DatabaseRuntimeException("Column with given name is not found in SQL query: " + columnName, HttpStatus.NOT_FOUND);

        }
    }

    public String getClob(String columnName) {
        try {
            if (isNull(columnName)) {
                return "";
            }

            return clobToString(resultSet.getClob(columnName));

        } catch (SQLException e) {
            throw new DatabaseRuntimeException("Column with given name is not found in SQL query: " + columnName, HttpStatus.NOT_FOUND);

        }

    }

    private String clobToString(Clob clob) {
        if (clob == null) {
            return "";
        }

        try {
            return clob.getSubString(1, (int) clob.length());
        } catch (SQLException e) {
            System.out.println("SQL Error while converting clob to string");
            throw new DatabaseRuntimeException("SQL Error while converting clob to string", HttpStatus.NOT_FOUND);
        }
    }

    public LocalDate getLocalDate(String columnName) {
        try {
            return isNull(columnName) ? null : resultSet.getDate(columnName).toLocalDate();
        } catch (SQLException e) {
            throw new DatabaseRuntimeException("Column with given name is not found in SQL query: " + columnName, HttpStatus.NOT_FOUND);

        }
    }

    public LocalDateTime getLocalDateTime(String columnName) {
        try {
            return isNull(columnName) ? null : resultSet.getTimestamp(columnName).toLocalDateTime();
        } catch (SQLException e) {
            throw new DatabaseRuntimeException("Column with given name is not found in SQL query: " + columnName, HttpStatus.NOT_FOUND);

        }
    }

    /**
     * Kolon değeri null ise null döner, değil ise getBooleanTF methodu gibi
     * davranır.
     * 
     * @param columnName
     * @return
     * @throws SQLException
     */
    public Boolean getBoolean(String columnName) {
        try {
            return isNull(columnName) ? null : "T".equals(resultSet.getString(columnName));
        } catch (SQLException e) {
            throw new DatabaseRuntimeException("Column with given name is not found in SQL query: " + columnName, HttpStatus.NOT_FOUND);

        }
    }

    /**
     * Kolon değeri 'T' ise true döner, diğer durumlarda false döner.
     * 
     * @param columnName
     * @return
     * @throws SQLException
     */
    public boolean getBooleanTF(String columnName) {
        try {
            return "T".equals(resultSet.getString(columnName));
        } catch (SQLException e) {
            throw new DatabaseRuntimeException("Column with given name is not found in SQL query: " + columnName, HttpStatus.NOT_FOUND);

        }
    }

    /***
     * Kolon değeri sıfırdan büyükse true döner, sıfır ve null ise false döner.
     *
     * @param columnName
     * @return
     * @throws SQLException
     */
    public boolean getBoolean1v0(String columnName) {
        try {
            if (isNull(columnName)) {
                return false;
            } else {
                return (resultSet.getInt(columnName) > 0);
            }
        } catch (SQLException e) {
            throw new DatabaseRuntimeException("Column with given name is not found in SQL query: " + columnName, HttpStatus.NOT_FOUND);

        }
    }

    public LocalTime getLocalTime(String columnName) {
        try {
            return isNull(columnName) ? null : LocalTime.parse(resultSet.getString(columnName));
        } catch (SQLException e) {
            throw new DatabaseRuntimeException("Column with given name is not found in SQL query: " + columnName, HttpStatus.NOT_FOUND);

        }
    }

    public boolean next() {
        try {
            return resultSet.next();
        } catch (SQLException e) {
            throw new DatabaseRuntimeException("SQL Error  " + e.getLocalizedMessage(), HttpStatus.NOT_FOUND);

        }
    }

    public boolean hasColumn(String columnName) {
        try {
            return resultSet.findColumn(columnName) > -1;
        } catch (SQLException e) {
            return false;
        }
    }

    public String getColumnName(Integer columnIndex) {

        try {
            return resultSet.getMetaData().getColumnLabel(columnIndex);
        } catch (SQLException e) {
            throw new DatabaseRuntimeException("Column with given index can't found " + columnIndex, HttpStatus.NOT_FOUND);

        }
    }

    public List<String> getStringList(String columnName, String splitRegex) {

        try {
            return isNull(columnName) ? Collections.emptyList() : Arrays.asList(resultSet.getString(columnName).split(splitRegex));
        } catch (SQLException e) {
            throw new DatabaseRuntimeException("Column with given name is not found in SQL query: " + columnName, HttpStatus.NOT_FOUND);

        }
    }

    public Character getCharacter(String columnName) {
        String result;
        try {
            result = resultSet.getString(columnName);
            return (result != null && !result.isEmpty()) ? result.charAt(0) : null;
        } catch (SQLException e) {
            throw new DatabaseRuntimeException("Column with given name is not found in SQL query: " + columnName, HttpStatus.NOT_FOUND);

        }
    }

}
