package com.izettle.app.resources;

import com.izettle.app.db.*;

import javax.ws.rs.*;

@Path("/emptyTables")
public class EmptyTablesResource {

	private final UserDAO userDAO;
	private final UserSessionDAO userSessionDAO;

	public EmptyTablesResource(UserDAO userDAO, UserSessionDAO userSessionDAO) {
		this.userDAO = userDAO;
		this.userSessionDAO = userSessionDAO;
	}

	@POST
	public void execute() {
		userDAO.dropUserTable();
		userDAO.createUserTable();
		userSessionDAO.dropUserSessionTable();
		userSessionDAO.createUserSessionTable();
	}
}
