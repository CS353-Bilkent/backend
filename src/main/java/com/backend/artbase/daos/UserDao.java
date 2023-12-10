package com.backend.artbase.daos;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import com.backend.artbase.core.CustomJdbcTemplate;
import com.backend.artbase.core.CustomSqlParameters;
import com.backend.artbase.core.ResultSetWrapper;
import com.backend.artbase.entities.User;
import com.backend.artbase.entities.UserType;
import com.backend.artbase.errors.UserRuntimeException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class UserDao {

    private final CustomJdbcTemplate jdbcTemplate;

    public void saveUser(User user) {

        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("user_name", user.getUserName());
        params.put("email", user.getEmail());
        params.put("user_password", user.getUserPassword());
        params.put("user_type", String.valueOf(user.getUserType().getCode()));

        //@formatter:off
        String sql = 
            "INSERT INTO users (user_name, email, user_password, user_type) "
            + "VALUES (:user_name, :email, :user_password, :user_type)";
        //@formatter:on

        jdbcTemplate.update(sql, params);
    }

    public User getUser(Integer user_id) {
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("user_id", user_id);

        //@formatter:off
        String sql =
            "SELECT u.user_id, u.user_name, u.email, u.user_password ,u.user_type"+
             "FROM USERS u WHERE u.user_id = :user_id";
        //@formatter:on

        return jdbcTemplate.queryForObject(sql, params, (rs, rnum) -> {
            ResultSetWrapper rsw = new ResultSetWrapper(rs);

            //@formatter:off
            return User.builder()
            .userId(rsw.getInteger("user_id"))
            .email(rsw.getString("email"))
            .userName(rsw.getString("user_name"))
            .userPassword(rsw.getString("user_password"))
            .userType(UserType.valueOf(rsw.getString("user_type")))
            .build();
            //@formatter:on
        });
    }

    public User getUserByUsername(String username) {
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("user_name", username);

    //@formatter:off
    String sql =
        "SELECT u.user_id, u.user_name, u.email, u.user_password, u.user_type " +
        "FROM USERS u WHERE u.user_name = :user_name";
    //@formatter:on

        try {
            return jdbcTemplate.queryForObject(sql, params, (rs, rnum) -> {
                ResultSetWrapper rsw = new ResultSetWrapper(rs);

            //@formatter:off
            return User.builder()
                .userId(rsw.getInteger("user_id"))
                .email(rsw.getString("email"))
                .userName(rsw.getString("user_name"))
                .userPassword(rsw.getString("user_password"))
                .userType(UserType.fromCode(rsw.getString("user_type")))
                .build();
            //@formatter:on
            });
        } catch (EmptyResultDataAccessException ex) {
            throw new UserRuntimeException("There is no user found with given username", HttpStatus.NOT_FOUND);
        }
    }

    public User getUserByEmail(String email) {
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("email", email);

        //@formatter:off
            String sql =
                "SELECT u.user_id, u.user_name, u.email, u.user_password, u.user_type " +
                "FROM USERS u WHERE u.email = :email";
            //@formatter:on

        try {
            return jdbcTemplate.queryForObject(sql, params, (rs, rnum) -> {
                ResultSetWrapper rsw = new ResultSetWrapper(rs);

                //@formatter:off
                    return User.builder()
                        .userId(rsw.getInteger("user_id"))
                        .email(rsw.getString("email"))
                        .userName(rsw.getString("user_name"))
                        .userPassword(rsw.getString("user_password"))
                        .userType(UserType.valueOf(rsw.getString("user_type")))
                        .build();
                    //@formatter:on
            });
        } catch (EmptyResultDataAccessException ex) {
            throw new UserRuntimeException("There is no user found with the given email", HttpStatus.NOT_FOUND);
        }

    }

    public User getUserByEmailOrUsername(String emailOrUsername) {
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("emailOrUsername", emailOrUsername);

        //@formatter:off
        String sql =
            "SELECT u.user_id, u.user_name, u.email, u.user_password, u.user_type " +
            "FROM USERS u WHERE u.email = :emailOrUsername OR u.user_name = :emailOrUsername";
        //@formatter:on

        try {
            return jdbcTemplate.queryForObject(sql, params, (rs, rnum) -> {
                ResultSetWrapper rsw = new ResultSetWrapper(rs);

                //@formatter:off
                return User.builder()
                    .userId(rsw.getInteger("user_id"))
                    .email(rsw.getString("email"))
                    .userName(rsw.getString("user_name"))
                    .userPassword(rsw.getString("user_password"))
                    .userType(UserType.valueOf(rsw.getString("user_type")))
                    .build();
                //@formatter:on
            });
        } catch (EmptyResultDataAccessException ex) {
            throw new UserRuntimeException("There is no user found with the given email or username", HttpStatus.NOT_FOUND);
        }
    }
}
