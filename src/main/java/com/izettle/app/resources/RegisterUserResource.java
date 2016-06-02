package com.izettle.app.resources;

import com.izettle.app.api.StandardResult;
import com.izettle.app.db.UserDAO;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.atomic.AtomicLong;

@Path("/register")
@Produces(MediaType.APPLICATION_JSON)
public class RegisterUserResource {

	private final AtomicLong counter;
	private final UserDAO userDAO;

	public RegisterUserResource(UserDAO userDAO) {
		this.userDAO = userDAO;
		this.counter = new AtomicLong();
	}

	@POST
	public StandardResult register(@QueryParam("username") String username, @QueryParam("password") String password) {
		boolean successful = false;
		long id = counter.incrementAndGet();
		if (userDAO.findUserByUsername(username) == null) {
			userDAO.insert(id, username, password);
			successful = true;
		}
		return new StandardResult(id, successful);
	}
}
