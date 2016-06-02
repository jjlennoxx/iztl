package com.izettle.app;

import com.izettle.app.db.*;
import com.izettle.app.resources.*;
import io.dropwizard.*;
import io.dropwizard.jdbi.*;
import io.dropwizard.jdbi.bundles.*;
import io.dropwizard.setup.*;
import org.skife.jdbi.v2.*;

public class App extends Application<AppConfiguration> {

	@Override
	public String getName() {
		return "iZettle-app";
	}

	@Override
	public void initialize(Bootstrap<AppConfiguration> bootstrap) {
		bootstrap.addBundle(new DBIExceptionsBundle());
	}

	@Override
	public void run(AppConfiguration configuration, Environment environment) {
		final DBIFactory factory = new DBIFactory();
		final DBI jdbi = factory.build(environment, configuration.getDataSourceFactory(), "h2db");
		final UserDAO userDAO = jdbi.onDemand(UserDAO.class);
		final UserSessionDAO userSessionDAO = jdbi.onDemand(UserSessionDAO.class);

		Integer sessionTimeoutInSeconds = Integer.valueOf(configuration.getSessionTimeoutInSeconds());
		environment.jersey().register(new RegisterUserResource(userDAO));
		environment.jersey().register(new AuthenticateUserResource(userDAO, userSessionDAO, sessionTimeoutInSeconds));
		environment.jersey().register(new ListTimestampsResource(userDAO, userSessionDAO));
		environment.jersey().register(new EmptyTablesResource(userDAO, userSessionDAO));
//		environment.healthChecks().register("template", new TemplateHealthCheck(configuration.getTemplate()));

		userDAO.createUserTable();
		userSessionDAO.createUserSessionTable();
	}

	public static void main(String[] args) throws Exception {
		new App().run(args);
	}
}
