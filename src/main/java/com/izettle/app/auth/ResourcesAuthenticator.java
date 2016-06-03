package com.izettle.app.auth;

import com.google.common.base.Optional;
import com.google.common.collect.*;
import io.dropwizard.auth.*;
import io.dropwizard.auth.basic.*;

import java.util.Set;
import java.util.Map;

public class ResourcesAuthenticator implements Authenticator<BasicCredentials, PrincipalUser> {

    // mapping user -> roles
    private static final Map<String, Set<String>> VALID_USERS = ImmutableMap.of(
        "user", ImmutableSet.of("USER"),
        "admin", ImmutableSet.of("ADMIN", "USER")
    );

    @Override
    public Optional<PrincipalUser> authenticate(BasicCredentials credentials) throws AuthenticationException {
        if (VALID_USERS.containsKey(credentials.getUsername()) && "secret".equals(credentials.getPassword())) {
            return Optional.of(new PrincipalUser(credentials.getUsername(),
                                                 VALID_USERS.get(credentials.getUsername())));
        }
        return Optional.absent();
    }
}
