package com.izettle.app;

import com.izettle.app.auth.*;
import com.izettle.app.db.*;
import com.izettle.app.resources.*;
import io.dropwizard.*;
import io.dropwizard.auth.*;
import io.dropwizard.auth.basic.*;
import io.dropwizard.jdbi.*;
import io.dropwizard.jdbi.bundles.*;
import io.dropwizard.setup.*;
import org.glassfish.jersey.server.filter.*;
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

		prepareDatabase(userDAO, userSessionDAO);
		registerSecurityComponents(environment);
		registerResourceEndpoints(configuration, environment, userDAO, userSessionDAO);
	}

	private void prepareDatabase(UserDAO userDAO, UserSessionDAO userSessionDAO) {
		userDAO.createUserTable();
		userSessionDAO.createUserSessionTable();
	}

	private void registerSecurityComponents(Environment environment) {
		environment.jersey().register(new AuthDynamicFeature(
				new BasicCredentialAuthFilter.Builder<PrincipalUser>()
						.setAuthenticator(new ResourcesAuthenticator())
						.setAuthorizer(new ResourcesAuthorizer())
						.setRealm("iZettle Realm")
						.buildAuthFilter()));
		environment.jersey().register(RolesAllowedDynamicFeature.class);
		environment.jersey().register(new AuthValueFactoryProvider.Binder<>(PrincipalUser.class));
	}

	private void registerResourceEndpoints(AppConfiguration configuration, Environment environment, UserDAO userDAO,
	                                       UserSessionDAO userSessionDAO) {
		Integer sessionTimeoutInSeconds = Integer.valueOf(configuration.getSessionTimeoutInSeconds());
		environment.jersey().register(new RegisterUserResource(userDAO));
		environment.jersey().register(new AuthenticateUserResource(userDAO, userSessionDAO, sessionTimeoutInSeconds));
		environment.jersey().register(new ListTimestampsResource(userDAO, userSessionDAO));
		environment.jersey().register(new EmptyTablesResource(userDAO, userSessionDAO));
	}

	public static void main(String[] args) throws Exception {
		new App().run(args);
	}
}
