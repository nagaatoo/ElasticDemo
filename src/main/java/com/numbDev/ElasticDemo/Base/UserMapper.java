package com.numbDev.ElasticDemo.Base;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserMapper implements RowMapper {
    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setTitle(resultSet.getString("title"));
        user.setContent(resultSet.getString("content"));

        return user;
    }
}
