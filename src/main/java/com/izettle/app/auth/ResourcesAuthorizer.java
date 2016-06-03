package com.izettle.app.auth;

import io.dropwizard.auth.*;

public class ResourcesAuthorizer implements Authorizer<PrincipalUser> {

	@Override
	public boolean authorize(PrincipalUser principalUser, String role) {
		return principalUser.getRoles() != null && principalUser.getRoles().contains(role);
	}
}
