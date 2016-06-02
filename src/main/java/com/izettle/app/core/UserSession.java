package com.izettle.app.core;

import java.sql.Timestamp;

public class UserSession {

    private final Long id;
    private final Long user_id;
    private final Timestamp timestamp;

    public UserSession(Long id, Long user_id, Timestamp timestamp) {
        this.id = id;
        this.user_id = user_id;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserSession that = (UserSession) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (user_id != null ? !user_id.equals(that.user_id) : that.user_id != null) return false;
        return timestamp != null ? timestamp.equals(that.timestamp) : that.timestamp == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (user_id != null ? user_id.hashCode() : 0);
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        return result;
    }
}
