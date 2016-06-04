package com.izettle.app.auth;

import java.security.*;
import java.util.*;

public class PrincipalUser implements Principal {

    private final String name;
    private final String password;
    private final Set<String> roles;

    public PrincipalUser(String name, String password) {
        this.name = name;
        this.password = password;
        this.roles = Collections.EMPTY_SET;
    }

    public PrincipalUser(String name, String password, Set<String> roles) {
        this.name = name;
        this.password = password;
        this.roles = roles;
    }

    public PrincipalUser(Object authorizedUser) {
        if (authorizedUser instanceof Map) {
            Map<String, Object> authorizedUserMap = (Map) authorizedUser;
            if (authorizedUserMap.get("roles") instanceof List) {
                this.name = (String) authorizedUserMap.get("name");
                this.password = (String) authorizedUserMap.get("password");
                this.roles = new HashSet<>(((List) authorizedUserMap.get("roles")));
                return;
            }
        }
        this.name = null;
        this.password = null;
        this.roles = Collections.EMPTY_SET;
    }

    public int getId() {
        return (int) (Math.random() * 100);
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public Set<String> getRoles() {
        return roles;
    }
}
