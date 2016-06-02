package com.izettle.app.resources;

import com.izettle.app.api.*;
import com.izettle.app.core.*;
import com.izettle.app.db.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;
import java.util.concurrent.atomic.*;

@Path("/listTimestamps")
@Produces(MediaType.APPLICATION_JSON)
public class ListTimestampsResource {

    private final UserDAO userDAO;
    private final UserSessionDAO userSessionDAO;
    private final AtomicLong counter;

    public ListTimestampsResource(UserDAO userDAO, UserSessionDAO userSessionDAO) {
        this.userDAO = userDAO;
        this.userSessionDAO = userSessionDAO;
        this.counter = new AtomicLong();
    }

    @POST
    public TimestampsResult register(@QueryParam("username") String username, @QueryParam("sessionId") Long sessionId) {
        Boolean successful = false;
        List<UserSession> userSessions = Collections.EMPTY_LIST;

        Long userId = userDAO.findIdByUsername(username);
        if (userId != null) {
            long actualSessionId = userSessionDAO.findIdByUserId(userId);
            if (actualSessionId == sessionId) {
                userSessions = userSessionDAO.findLastUserTimestamps(userId, 5);
                successful = true;
            }
        }
        return new TimestampsResult(counter.incrementAndGet(), successful, userSessions);
    }
}
