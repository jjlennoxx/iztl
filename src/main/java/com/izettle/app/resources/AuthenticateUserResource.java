package com.izettle.app.resources;

import com.izettle.app.api.*;
import com.izettle.app.core.*;
import com.izettle.app.db.*;
import com.izettle.app.security.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.sql.*;
import java.util.concurrent.atomic.*;

@Path("/authenticate")
@Produces(MediaType.APPLICATION_JSON)
public class AuthenticateUserResource {

    private final UserDAO userDAO;
    private final UserSessionDAO userSessionDAO;
    private final PasswordAuthentication crypto;
    private final Integer sessionTimeoutInSeconds;
    private final AtomicLong counter;

    public AuthenticateUserResource(UserDAO userDAO, UserSessionDAO userSessionDAO, Integer sessionTimeoutInSeconds) {
        this.userDAO = userDAO;
        this.userSessionDAO = userSessionDAO;
        this.crypto = new PasswordAuthentication();
        this.sessionTimeoutInSeconds = sessionTimeoutInSeconds;
        this.counter = new AtomicLong();
    }

    @POST
    public AuthenticationResult register(@QueryParam("username") String username,
                                         @QueryParam("password") String password) {
        Boolean successful = false;
        Long sessionId = null;

        User user = userDAO.findUserByUsername(username);
        if (user != null) {
            String token = user.getToken();
            if (crypto.authenticate(password.toCharArray(), token)) {
                long nowMinusSessionTimeout = System.currentTimeMillis() - (sessionTimeoutInSeconds * 1000);
                Timestamp timestamp = new Timestamp(nowMinusSessionTimeout);
                sessionId = getSessionId(user, timestamp);
                successful = true;
            }
        }
        return new AuthenticationResult(counter.incrementAndGet(), successful, sessionId);
    }

    private Long getSessionId(User user, Timestamp timestamp) {
        Long sessionId = userSessionDAO.findIdByUserIdAndTimeout(user.getId(), timestamp);
        if (sessionId == null) {
            sessionId = userSessionDAO.createUserSessionForUserId(user.getId());
        }
        return sessionId;
    }
}
