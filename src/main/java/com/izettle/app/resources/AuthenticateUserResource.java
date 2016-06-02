package com.izettle.app.resources;

import com.izettle.app.api.*;
import com.izettle.app.db.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.sql.*;
import java.util.concurrent.atomic.*;

@Path("/authenticate")
@Produces(MediaType.APPLICATION_JSON)
public class AuthenticateUserResource {

    private final UserDAO userDAO;
    private final UserSessionDAO userSessionDAO;
    private final Integer sessionTimeoutInSeconds;
    private final AtomicLong counter;

    public AuthenticateUserResource(UserDAO userDAO, UserSessionDAO userSessionDAO, Integer sessionTimeoutInSeconds) {
        this.userDAO = userDAO;
        this.userSessionDAO = userSessionDAO;
        this.sessionTimeoutInSeconds = sessionTimeoutInSeconds;
        this.counter = new AtomicLong();
    }

    @POST
    public AuthenticationResult register(@QueryParam("username") String username,
                                         @QueryParam("password") String password) {
        Boolean successful = false;
        Long sessionId = null;

        Long userId = userDAO.findIdByUsernameAndPassword(username, password);
        if (userId != null) {
            Timestamp nowMinusSessionTimeout = new Timestamp(
                    System.currentTimeMillis() - (sessionTimeoutInSeconds * 1000));
            sessionId = userSessionDAO.findIdByUserIdAndTimeout(userId, nowMinusSessionTimeout);
            if (sessionId == null) {
                sessionId = userSessionDAO.createUserSessionForUserId(userId);
            }
            successful = true;
        }

        return new AuthenticationResult(counter.incrementAndGet(), successful, sessionId);
    }
}
