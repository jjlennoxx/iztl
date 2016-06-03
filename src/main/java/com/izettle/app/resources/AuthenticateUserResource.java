package com.izettle.app.resources;

import com.izettle.app.api.*;
import com.izettle.app.auth.*;
import com.izettle.app.core.*;
import com.izettle.app.db.*;
import org.hibernate.validator.constraints.*;
import org.slf4j.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.sql.*;
import java.util.concurrent.atomic.*;

@Path("/authenticate")
@Produces(MediaType.APPLICATION_JSON)
public class AuthenticateUserResource {
    private static final Logger log = LoggerFactory.getLogger(AuthenticateUserResource.class);

    private final UserDAO userDAO;
    private final UserSessionDAO userSessionDAO;
    private final PasswordAuthentication crypto;
    private final Integer sessionTimeoutInSeconds;
    private final AtomicLong counter;

    public AuthenticateUserResource(UserDAO userDAO, UserSessionDAO userSessionDAO, Integer sessionTimeoutInSeconds) {
        this.userDAO = userDAO;
        this.userSessionDAO = userSessionDAO;
        this.sessionTimeoutInSeconds = sessionTimeoutInSeconds;
        this.crypto = new PasswordAuthentication();
        this.counter = new AtomicLong();
    }

    @POST
    public AuthenticationResult execute(@QueryParam("username") @NotBlank String username,
                                        @QueryParam("password") @NotBlank String password) {
        Boolean successful = false;
        Long sessionId = null;

        User user = userDAO.findUserByUsername(username);
        if (user != null) {
            String token = user.getToken();
            if (crypto.authenticate(password.toCharArray(), token)) {
                long nowMinusSessionTimeout = System.currentTimeMillis() - (sessionTimeoutInSeconds * 1000);
                Timestamp timeThreshold = new Timestamp(nowMinusSessionTimeout);
                sessionId = getSessionId(user, timeThreshold);
                successful = true;
            }
        }
        return new AuthenticationResult(counter.incrementAndGet(), successful, sessionId);
    }

    private Long getSessionId(User user, Timestamp timeThreshold) {
        long userId = user.getId();
        Long sessionId = userSessionDAO.findIdByUserIdAndThreshold(userId, timeThreshold);
        if (sessionId == null) {
            sessionId = userSessionDAO.createUserSessionForUserId(userId);
            log.info("New user session for userId {}", userId);
        } else {
            log.info("User session for userId {} still active!", userId);
        }
        return sessionId;
    }
}
