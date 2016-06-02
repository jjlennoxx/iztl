package com.izettle.app.resources;

import com.izettle.app.api.StandardResult;
import com.izettle.app.db.UserDAO;
import com.izettle.app.security.*;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.atomic.AtomicLong;

@Path("/register")
@Produces(MediaType.APPLICATION_JSON)
public class RegisterUserResource {

	private final UserDAO userDAO;
	private final PasswordAuthentication crypto;
	private final AtomicLong counter;

	public RegisterUserResource(UserDAO userDAO) {
		this.userDAO = userDAO;
		this.crypto = new PasswordAuthentication();
		this.counter = new AtomicLong();
	}

	@POST
	public StandardResult register(@QueryParam("username") String username, @QueryParam("password") String password) {
		boolean successful = false;
		long id = counter.incrementAndGet();
		if (userDAO.findUserByUsername(username) == null) {
			String token = crypto.hash(password.toCharArray());
			userDAO.insertNewUser(id, username, token);
			successful = true;
		}
		return new StandardResult(id, successful);
	}
}
