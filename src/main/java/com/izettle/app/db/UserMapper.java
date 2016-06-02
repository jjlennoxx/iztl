package com.izettle.app.db;

import com.izettle.app.core.User;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements ResultSetMapper<User> {

    public User map(int i, ResultSet rs, StatementContext statementContext) throws SQLException {
        return new User(rs.getLong("id"), rs.getString("username"), rs.getString("password"));
    }
}
