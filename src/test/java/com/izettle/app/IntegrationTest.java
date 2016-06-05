package com.izettle.app;

import com.izettle.app.api.*;
import com.izettle.app.auth.*;
import com.izettle.app.client.*;
import io.dropwizard.testing.*;
import io.dropwizard.testing.junit.*;
import org.glassfish.jersey.*;
import org.glassfish.jersey.client.authentication.*;
import org.junit.*;

import javax.ws.rs.client.*;
import java.io.*;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class IntegrationTest {

	private static final Integer SLEEP = 3000;
	private static final String TMP_FILE = createTempFile();
	private static final String CONFIG_PATH = ResourceHelpers.resourceFilePath("test-config.yml");

	@ClassRule
	public static final DropwizardAppRule<IztlAppConfig> RULE = new DropwizardAppRule<>(
			IztlApp.class,
			CONFIG_PATH,
			ConfigOverride.config("database.url", "jdbc:h2:" + TMP_FILE));

	private iZettleClient iztlClient;

	@Before
	public void setUp() throws Exception {
		SslConfigurator sslConfigurator = SslConfigurator.newInstance()
		                                                 .trustStoreFile("iztl.keystore")
		                                                 .trustStorePassword("Th3_iztl_s3cr3t");
		Client client = ClientBuilder.newBuilder()
		                             .sslContext(sslConfigurator.createSSLContext())
		                             .build();
		Optional<PrincipalUser> adminOptional = RULE.getConfiguration()
		                                            .getAuthorizedUsers()
		                                            .stream()
		                                            .filter(user -> user.getRoles().contains("ADMIN"))
		                                            .findFirst();
		if (adminOptional.isPresent()) {
			PrincipalUser admin = adminOptional.get();
			client.register(HttpAuthenticationFeature.basic(admin.getName(), admin.getPassword()));
		}
		iztlClient = new iZettleClient("https://localhost:" + RULE.getLocalPort(), client);
		iztlClient.registerUser("antonio", "ant0ni0");
	}

	@After
	public void tearDown() throws Exception {
		iztlClient.close();
	}

	private static String createTempFile() {
		try {
			return File.createTempFile("h2db", null).getAbsolutePath();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	@Test
	public void testRegisterUser() throws Exception {
		StandardResult result2 = iztlClient.registerUser("antonio", "ant0ni0");
		assertEquals(result2.isSuccessful(), false);

		StandardResult result3 = iztlClient.registerUser("antonia", "ant0nia");
		assertEquals(result3.isSuccessful(), true);
	}

	@Test
	public void testAuthenticateUser() {
		AuthenticationResult authResult1 = iztlClient.authenticateUser("antonio_lages", "ant0ni0");
		assertEquals(authResult1.isSuccessful(), false);
		assertEquals(authResult1.getSessionId(), null);

		AuthenticationResult authResult2 = iztlClient.authenticateUser("antonio", "ant0nioakjsbfjhsdc");
		assertEquals(authResult2.isSuccessful(), false);
		assertEquals(authResult2.getSessionId(), null);

		AuthenticationResult authResult3 = iztlClient.authenticateUser("antonio", "ant0ni0");
		assertEquals(authResult3.isSuccessful(), true);
		assertNotNull(authResult3.getSessionId());
	}

	@Test
	public void testListTimestamps() throws Exception {
		AuthenticationResult authResult = null;
		for (int i = 0; i <= 5; i++) {
			authResult = iztlClient.authenticateUser("antonio", "ant0ni0");
			assertEquals(authResult.isSuccessful(), true);
			assertNotNull(authResult.getSessionId());
		}

		TimestampsResult tStampsResult = iztlClient.listUserTimestamps("antonio", authResult.getSessionId());
		assertEquals(tStampsResult.isSuccessful(), true);
		assertNotNull(tStampsResult.getTimestampList());

		System.out.println("\ntimestamps:\n" + tStampsResult.getTimestampList() + "\n");
	}

	@Test
	public void testAdminListTimestamps() throws Exception {
		AuthenticationResult authResult;
		for (int i = 0; i <= 5; i++) {
			authResult = iztlClient.authenticateUser("antonio", "ant0ni0");
			assertEquals(authResult.isSuccessful(), true);
			assertNotNull(authResult.getSessionId());
		}

		TimestampsResult tStampsResult = iztlClient.adminListUserTimestamps("antonio");
		assertEquals(tStampsResult.isSuccessful(), true);
		assertNotNull(tStampsResult.getTimestampList());

		System.out.println("\ntimestamps:\n" + tStampsResult.getTimestampList() + "\n");
	}

	//	@Test
	public void liveTestListTimestamps() throws InterruptedException {
		StandardResult registerResult1 = iztlClient.registerUser("antonio", "ant0ni0");
		assertEquals(registerResult1.isSuccessful(), true);

		StandardResult registerResult2 = iztlClient.registerUser("rebecca", "r3b3cca");
		assertEquals(registerResult2.isSuccessful(), true);

		while (true) {
			authenticateAndListTimestamps("antonio", "ant0ni0");
			authenticateAndListTimestamps("rebecca", "r3b3cca");
		}
	}

	private void authenticateAndListTimestamps(String username, String password) throws InterruptedException {
		AuthenticationResult authResult = iztlClient.authenticateUser(username, password);
		assertEquals(authResult.isSuccessful(), true);
		TimestampsResult tStampsResult = iztlClient.listUserTimestamps(username, authResult.getSessionId());
		assertEquals(tStampsResult.isSuccessful(), true);
		assertNotNull(tStampsResult.getTimestampList());

		System.out.println("username: " + username + "\nsession_id: " + authResult.getSessionId() + "\ntimestamps:");
		tStampsResult.getTimestampList().stream().forEach(System.out::println);
		System.out.println();

		Thread.sleep(SLEEP);
	}
}
