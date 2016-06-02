package com.izettle.app.db;

import com.izettle.app.core.UserSession;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserSessionMapper implements ResultSetMapper<UserSession> {

    public UserSession map(int i, ResultSet rs, StatementContext statementContext) throws SQLException {
        return new UserSession(rs.getLong("id"), rs.getLong("user_id"), rs.getTimestamp("timestamp"));
    }
}
