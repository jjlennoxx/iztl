package com.izettle.app.resources;

import com.izettle.app.api.*;
import com.izettle.app.auth.*;
import com.izettle.app.db.*;
import org.hibernate.validator.constraints.*;
import org.slf4j.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.concurrent.atomic.*;

@Path("/register")
@Produces(MediaType.APPLICATION_JSON)
public class RegisterUserResource {
	private static final Logger log = LoggerFactory.getLogger(RegisterUserResource.class);

	private final UserDAO userDAO;
	private final PasswordAuthentication crypto;
	private final AtomicLong counter;

	public RegisterUserResource(UserDAO userDAO) {
		this.userDAO = userDAO;
		this.crypto = new PasswordAuthentication();
		this.counter = new AtomicLong();
	}

	@POST
	public StandardResult execute(@QueryParam("username") @NotBlank String username,
	                              @QueryParam("password") @NotBlank String password) {
		boolean successful = false;
		if (userDAO.findUserByUsername(username) == null) {
			String token = crypto.hash(password.toCharArray());
			long id = userDAO.createNewUser(username, token);
			log.info("New user with username {} and id {} registered!", username, id);
			successful = true;
		}
		return new StandardResult(counter.incrementAndGet(), successful);
	}
}
