package com.izettle.app.client;

import com.izettle.app.api.AuthenticationResult;
import com.izettle.app.api.Result;
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
        Result result1 = target.registerUser("antonio", "ant0ni0");
        assertEquals(result1.isSuccessful(), true);
        assertEquals(result1.getId(), 1);

        Result result2 = target.registerUser("antonio", "ant0ni0");
        assertEquals(result2.isSuccessful(), false);
        assertEquals(result2.getId(), 2);

        Result result3 = target.registerUser("antonia", "ant0nia");
        assertEquals(result3.isSuccessful(), true);
        assertEquals(result3.getId(), 3);
    }

    @Test
    public void authenticateUser() throws Exception {
        Result registerResult = target.registerUser("antonio", "ant0ni0");
        assertEquals(registerResult.isSuccessful(), true);
        assertEquals(registerResult.getId(), 1);

        Result authenticationResult1 = target.authenticateUser("antonio_lages", "ant0ni0");
        assertEquals(authenticationResult1.isSuccessful(), false);
        assertEquals(authenticationResult1.getId(), 2);

        Result authenticationResult2 = target.authenticateUser("antonio", "ant0nioakjsbfjhsdc");
        assertEquals(authenticationResult2.isSuccessful(), false);
        assertEquals(authenticationResult2.getId(), 3);

        AuthenticationResult authenticationResult3 = (AuthenticationResult) target.authenticateUser("antonio",
                                                                                                    "ant0ni0");
        assertEquals(authenticationResult3.isSuccessful(), true);
        assertEquals(authenticationResult3.getId(), 4);
        assertEquals(authenticationResult3.getSessionId(), 1);
    }

    @Test
    public void listUserTimestamps() throws Exception {

    }
}