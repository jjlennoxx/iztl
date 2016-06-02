package com.izettle.app.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

public class AuthenticationResult extends Result {

    private Long sessionId;

    public AuthenticationResult(Long id, Long sessionId, Boolean successful) {
        super(id, successful);
        this.sessionId = sessionId;
    }

    @JsonProperty
    public long getSessionId() {
        return sessionId;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", getId())
                .add("sessionId", getSessionId())
                .add("successful", isSuccessful())
                .toString();
    }
}
