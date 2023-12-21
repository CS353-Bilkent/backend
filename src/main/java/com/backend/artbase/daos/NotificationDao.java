package com.backend.artbase.daos;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.backend.artbase.core.CustomJdbcTemplate;
import com.backend.artbase.core.CustomSqlParameters;
import com.backend.artbase.core.ResultSetWrapper;
import com.backend.artbase.entities.Notification;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class NotificationDao {

    private final CustomJdbcTemplate jdbcTemplate;

    public List<Notification> getNotificationsByUserId(Integer userId) {

        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("user_id", userId);

        //@formatter:off
        String sql =
                "SELECT notification_id, content, user_id " +
                "FROM public.notification " +
                "WHERE user_id = :user_id";
        //@formatter:on

        return jdbcTemplate.query(sql, params, (rs, rnum) -> {
            ResultSetWrapper rsw = new ResultSetWrapper(rs);
            //@formatter:off
            return Notification.builder()
                    .notificationId(rsw.getInteger("notification_id"))
                    .content(rsw.getString("content"))
                    .userId(rsw.getInteger("user_id"))
                    .build();
            //@formatter:on
        });
    }

    public void deleteNotification(Integer notificationId) {
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("notification_id", notificationId);

        //@formatter:off
        String deleteSql =
                "DELETE FROM public.notification " +
                "WHERE notification_id = :notification_id";
        //@formatter:on

        jdbcTemplate.update(deleteSql, params);
    }
}
