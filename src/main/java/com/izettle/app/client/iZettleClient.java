package com.izettle.app.client;

import com.izettle.app.api.*;

import javax.ws.rs.client.*;

public class iZettleClient {

    private static final String registerResourceAddr = "/register";
    private static final String authenticateResourceAddr = "/authenticate";
    private static final String listTimestampsResourceAddr = "/listTimestamps";

    private final String addr;
    private final Client client;

    public iZettleClient(String addr) {
        this(addr, ClientBuilder.newClient());
    }

    public iZettleClient(String addr, Client client) {
        this.addr = addr;
        this.client = client;
    }

    public StandardResult registerUser(String username, String password) {
        return client.target(addr)
                     .path(registerResourceAddr)
                     .queryParam("username", username)
                     .queryParam("password", password)
                     .request()
                     .post(null)
                     .readEntity(StandardResult.class);
    }

    public AuthenticationResult authenticateUser(String username, String password) {
        return client.target(addr)
                     .path(authenticateResourceAddr)
                     .queryParam("username", username)
                     .queryParam("password", password)
                     .request()
                     .post(null)
                     .readEntity(AuthenticationResult.class);
    }

    public TimestampsResult listUserTimestamps(String username, Long sessionId) {
        return client.target(addr)
                     .path(listTimestampsResourceAddr)
                     .queryParam("username", username)
                     .queryParam("sessionId", sessionId)
                     .request()
                     .post(null)
                     .readEntity(TimestampsResult.class);
    }

    public TimestampsResult adminListUserTimestamps(String username) {
        return client.target(addr)
                     .path(listTimestampsResourceAddr + "/admin")
                     .queryParam("username", username)
                     .request()
                     .post(null)
                     .readEntity(TimestampsResult.class);
    }

    public void close() {
        client.close();
    }
}
