package com.izettle.app.api;

import com.google.common.base.*;

public class AuthenticationResult {

    private Long id;
    private Boolean successful;

    private Long sessionId;

    public AuthenticationResult() {}

    public AuthenticationResult(Long id, Boolean successful, Long sessionId) {
        this.id = id;
        this.successful = successful;
        this.sessionId = sessionId;
    }

    public Long getId() {
        return id;
    }

    public Boolean isSuccessful() {
        return successful;
    }

    public Long getSessionId() {
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
