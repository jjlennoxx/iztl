package com.izettle.app.auth;

import java.security.*;
import java.util.*;

public class PrincipalUser implements Principal {

    private final String name;
    private final Set<String> roles;

    public PrincipalUser(String name) {
        this.name = name;
        this.roles = null;
    }

    public PrincipalUser(String name, Set<String> roles) {
        this.name = name;
        this.roles = roles;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return (int) (Math.random() * 100);
    }

    public Set<String> getRoles() {
        return roles;
    }
}
