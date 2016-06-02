package com.izettle.app.client;

import com.izettle.app.api.AuthenticationResult;
import com.izettle.app.api.StandardResult;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class iZettleClientTest {

    private iZettleClient target;

    @Before
    public void setUp() throws Exception {
        target = new iZettleClient("http://localhost:8080");
    }

    @Test
    public void registerUser() throws Exception {
        StandardResult result1 = target.registerUser("antonio", "ant0ni0");
        assertEquals(result1.isSuccessful(), true);
        assertEquals(result1.getId(), new Long(1));

        StandardResult result2 = target.registerUser("antonio", "ant0ni0");
        assertEquals(result2.isSuccessful(), false);
        assertEquals(result2.getId(), new Long(2));

        StandardResult result3 = target.registerUser("antonia", "ant0nia");
        assertEquals(result3.isSuccessful(), true);
        assertEquals(result3.getId(), new Long(3));
    }

    @Test
    public void authenticateUser() throws Exception {
        StandardResult registerResult = target.registerUser("antonio", "ant0ni0");
        assertEquals(registerResult.getId(), new Long(1));
        assertEquals(registerResult.isSuccessful(), true);

        AuthenticationResult authenticationResult1 = target.authenticateUser("antonio_lages", "ant0ni0");
        assertEquals(authenticationResult1.getId(), new Long(1));
        assertEquals(authenticationResult1.isSuccessful(), false);
        assertEquals(authenticationResult1.getSessionId(), null);

        AuthenticationResult authenticationResult2 = target.authenticateUser("antonio", "ant0nioakjsbfjhsdc");
        assertEquals(authenticationResult2.getId(), new Long(2));
        assertEquals(authenticationResult2.isSuccessful(), false);
        assertEquals(authenticationResult1.getSessionId(), null);

        AuthenticationResult authenticationResult3 = target.authenticateUser("antonio", "ant0ni0");
        assertEquals(authenticationResult3.getId(), new Long(3));
        assertEquals(authenticationResult3.isSuccessful(), true);
        assertEquals(authenticationResult3.getSessionId(), new Long(1));
    }

    @Test
    public void listUserTimestamps() throws Exception {

    }
}