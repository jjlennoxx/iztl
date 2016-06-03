package com.izettle.app.client;

import com.izettle.app.api.*;
import org.junit.*;

import static org.junit.Assert.*;

public class iZettleClientTest {

    private iZettleClient target;
    private EmptyTablesTestClient emptyTables;

    @Before
    public void setUp() throws Exception {
        String addr = "http://localhost:8080";
        target = new iZettleClient(addr);
        emptyTables = new EmptyTablesTestClient(addr);
    }

    @Test
    public void registerUser() throws Exception {
        emptyTables.execute();

        StandardResult result1 = target.registerUser("antonio", "ant0ni0");
        assertEquals(result1.isSuccessful(), true);

        StandardResult result2 = target.registerUser("antonio", "ant0ni0");
        assertEquals(result2.isSuccessful(), false);

        StandardResult result3 = target.registerUser("antonia", "ant0nia");
        assertEquals(result3.isSuccessful(), true);
    }

    @Test
    public void authenticateUser() throws Exception {
        emptyTables.execute();

        StandardResult registerResult = target.registerUser("antonio", "ant0ni0");
        assertEquals(registerResult.isSuccessful(), true);

        AuthenticationResult authenticationResult1 = target.authenticateUser("antonio_lages", "ant0ni0");
        assertEquals(authenticationResult1.isSuccessful(), false);
        assertEquals(authenticationResult1.getSessionId(), null);

        AuthenticationResult authenticationResult2 = target.authenticateUser("antonio", "ant0nioakjsbfjhsdc");
        assertEquals(authenticationResult2.isSuccessful(), false);
        assertEquals(authenticationResult1.getSessionId(), null);

        AuthenticationResult authenticationResult3 = target.authenticateUser("antonio", "ant0ni0");
        assertEquals(authenticationResult3.isSuccessful(), true);
        assertEquals(authenticationResult3.getSessionId(), new Long(1));
    }

    @Test
    public void listUserTimestamps() throws Exception {
        emptyTables.execute();

        StandardResult registerResult1 = target.registerUser("antonio", "ant0ni0");
        assertEquals(registerResult1.isSuccessful(), true);

        StandardResult registerResult2 = target.registerUser("rebecca", "r3b3cca");
        assertEquals(registerResult2.isSuccessful(), true);

        while (true) {
            authenticateAndListTimestamps("antonio", "ant0ni0");
            authenticateAndListTimestamps("rebecca", "r3b3cca");
        }
    }

    private void authenticateAndListTimestamps(String username, String password) throws InterruptedException {
        AuthenticationResult authenticationResult = target.authenticateUser(username, password);
        assertEquals(authenticationResult.isSuccessful(), true);

        TimestampsResult timestampsResult = target.listUserTimestamps(username, authenticationResult.getSessionId());
        assertEquals(timestampsResult.isSuccessful(), true);
        assertNotNull(timestampsResult.getTimestampList());

        System.out.println("username: " + username
                                   + "\nsession_id: " + authenticationResult.getSessionId()
                                   + "\ntimestamps:");
        timestampsResult.getTimestampList().stream().forEach(System.out::println);
        System.out.println();

        Thread.sleep(3000);
    }
}