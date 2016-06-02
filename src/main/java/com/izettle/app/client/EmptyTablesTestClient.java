package com.izettle.app.client;

import javax.ws.rs.client.*;

public class EmptyTablesTestClient {

    private static final String emptyTablesResourceAddr = "/emptyTables";

    private final String addr;
    private final Client client;

    public EmptyTablesTestClient(String addr) {
        this.addr = addr;
        this.client = ClientBuilder.newClient();
    }

    public void execute() {
        client.target(addr + emptyTablesResourceAddr).request().post(null);
    }
}
