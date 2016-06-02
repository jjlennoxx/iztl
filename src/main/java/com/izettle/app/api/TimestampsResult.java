package com.izettle.app.api;

import com.google.common.base.MoreObjects;
import com.izettle.app.core.UserSession;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TimestampsResult extends Result {

    private List<Timestamp> timestampList;

    public TimestampsResult(Long id, Boolean successful, final List<UserSession> userSessions) {
        super(id, successful);
        Collections.reverse(userSessions);
        this.timestampList = userSessions.stream()
                                         .map(userSession -> new Timestamp(userSession.getTimestamp().getTime()))
                                         .collect(Collectors.toList());
    }

    public List<Timestamp> getTimestampList() {
        return timestampList;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", getId())
                .add("timestamps", getTimestampList())
                .add("successful", isSuccessful())
                .toString();
    }
}
