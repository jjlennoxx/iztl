package com.izettle.app.resources;

import com.izettle.app.api.*;
import com.izettle.app.auth.*;
import com.izettle.app.core.*;
import com.izettle.app.db.*;
import io.dropwizard.auth.*;
import org.hibernate.validator.constraints.*;

import javax.annotation.security.*;
import javax.validation.constraints.*;
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
    private final AtomicLong adminCounter;

    public ListTimestampsResource(UserDAO userDAO, UserSessionDAO userSessionDAO) {
        this.userDAO = userDAO;
        this.userSessionDAO = userSessionDAO;
        this.counter = new AtomicLong();
        this.adminCounter = new AtomicLong();
    }

    @POST
    public TimestampsResult execute(@QueryParam("username") @NotBlank String username,
                                    @QueryParam("sessionId") @NotNull Long sessionId) {
        Boolean successful = false;
        List<UserSession> userSessions = Collections.EMPTY_LIST;

        Long userId = userDAO.findIdByUsername(username);
        if (userId != null) {
            Long actualSessionId = userSessionDAO.findIdByUserId(userId);
            if (actualSessionId != null && actualSessionId.equals(sessionId)) {
                userSessions = userSessionDAO.findLastUserTimestamps(userId, 5);
                successful = true;
            }
        }
        return new TimestampsResult(counter.incrementAndGet(), successful, userSessions);
    }

    @RolesAllowed("ADMIN")
    @POST
    @Path("admin")
    public TimestampsResult executeAdmin(@Auth PrincipalUser principalUser,
                                         @QueryParam("username") @NotBlank String username) {
        Boolean successful = false;
        List<UserSession> userSessions = Collections.EMPTY_LIST;

        Long userId = userDAO.findIdByUsername(username);
        if (userId != null) {
            userSessions = userSessionDAO.findLastUserTimestamps(userId, 5);
            successful = true;
        }
        return new TimestampsResult(adminCounter.incrementAndGet(), successful, userSessions);
    }
}
