package com.izettle.app.resources;

import com.izettle.app.api.AuthenticationResult;
import com.izettle.app.api.Result;
import com.izettle.app.db.UserDAO;
import com.izettle.app.db.UserSessionDAO;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.sql.Timestamp;
import java.util.concurrent.atomic.AtomicLong;

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
    public Result register(@QueryParam("username") String username, @QueryParam("password") String password) {
        Long userId = userDAO.findIdByUsernameAndPassword(username, password);
        if (userId != null) {
            Timestamp nowMinusSessionTimeout = new Timestamp(
                    System.currentTimeMillis() - (sessionTimeoutInSeconds * 1000));
            Long sessionId = userSessionDAO.findIdByUserIdAndTimeout(userId, nowMinusSessionTimeout);
            if (sessionId == null) {
                sessionId = userSessionDAO.createUserSessionForUserId(userId);
            }
            return new AuthenticationResult(counter.incrementAndGet(), sessionId, true);
        }
        return new Result(counter.incrementAndGet(), false);
    }
}
