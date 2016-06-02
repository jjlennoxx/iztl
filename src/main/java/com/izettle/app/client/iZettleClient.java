package com.izettle.app.client;

import com.izettle.app.api.Result;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

public class iZettleClient {

    private static final String registerResourceAddr = "/register";
    private static final String authenticateResourceAddr = "/authenticate";
    private static final String listTimestampsResourceAddr = "/listTimestamps";

    private final String addr;
    private final Client client;

    public iZettleClient(String addr) {
        this.addr = addr;
        this.client = ClientBuilder.newClient();
    }

    public Result registerUser(String username, String password) {
        return client.target(addr + registerResourceAddr)
                     .queryParam("username", username)
                     .queryParam("password", password)
                     .request()
                     .post(null)
                     .readEntity(Result.class);
    }

    public Result authenticateUser(String username, String password) {
        return client.target(addr + authenticateResourceAddr)
                     .queryParam("username", username)
                     .queryParam("password", password)
                     .request()
                     .post(null)
                     .readEntity(Result.class);
    }

    public Result listUserTimestamps(String username, Long sessionId) {
        return client.target(addr + listTimestampsResourceAddr)
                     .queryParam("username", username)
                     .queryParam("sessionId", sessionId)
                     .request()
                     .post(null)
                     .readEntity(Result.class);
    }
}
