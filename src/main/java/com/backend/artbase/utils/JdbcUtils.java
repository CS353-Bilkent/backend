package com.backend.artbase.utils;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.stereotype.Component;

@Component
public class JdbcUtils {

    public static Timestamp convert(LocalDateTime localDateTime) {
        return localDateTime == null ? null : Timestamp.valueOf(localDateTime);
    }

    public static Timestamp convert(java.util.Date date) {
        return date == null ? null : new Timestamp(date.getTime());
    }

    public static Date convert(LocalDate localDate) {
        return localDate == null ? null : Date.valueOf(localDate);
    }

}
