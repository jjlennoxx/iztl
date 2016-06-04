package com.izettle.app.auth;

import com.google.common.base.Optional;
import io.dropwizard.auth.*;
import io.dropwizard.auth.basic.*;

import java.util.*;
import java.util.stream.*;

public class ResourcesAuthenticator implements Authenticator<BasicCredentials, PrincipalUser> {

    private final Map<String, PrincipalUser> VALID_USERS;

    public ResourcesAuthenticator(List<PrincipalUser> authorizedUsers) {
        this.VALID_USERS = authorizedUsers.stream().collect(Collectors.toMap(PrincipalUser::getName, p -> p));
    }

    @Override
    public Optional<PrincipalUser> authenticate(BasicCredentials credentials) throws AuthenticationException {
        Optional<PrincipalUser> principalOptional = Optional.fromNullable(VALID_USERS.get(credentials.getUsername()));
        if (principalOptional.isPresent() && principalOptional.get().getPassword().equals(credentials.getPassword())) {
            return principalOptional;
        }
        return Optional.absent();
    }
}
