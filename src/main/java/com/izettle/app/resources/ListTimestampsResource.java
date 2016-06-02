package com.izettle.app.resources;

import com.izettle.app.api.Result;
import com.izettle.app.api.TimestampsResult;
import com.izettle.app.core.UserSession;
import com.izettle.app.db.UserDAO;
import com.izettle.app.db.UserSessionDAO;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Path("/listTimestamps")
@Produces(MediaType.APPLICATION_JSON)
public class ListTimestampsResource {

    private final UserDAO userDAO;
    private final UserSessionDAO userSessionDAO;
    private final Integer sessionTimeoutInSeconds;
    private final AtomicLong counter;

    public ListTimestampsResource(UserDAO userDAO, UserSessionDAO userSessionDAO, Integer sessionTimeoutInSeconds) {
        this.userDAO = userDAO;
        this.userSessionDAO = userSessionDAO;
        this.sessionTimeoutInSeconds = sessionTimeoutInSeconds;
        this.counter = new AtomicLong();
    }

    @POST
    public Result register(@QueryParam("username") String username, @QueryParam("sessionId") Long sessionId) {
        Long userId = userDAO.findIdByUsername(username);
        if (userId != null) {
            long actualSessionId = userSessionDAO.findIdByUserId(userId);
            if (actualSessionId == sessionId) {
                List<UserSession> userSessions = userSessionDAO.findLastUserTimestamps(userId, 5);
                return new TimestampsResult(counter.incrementAndGet(), true, userSessions);
            }
        }
        return new Result(counter.incrementAndGet(), false);
    }
}
