package com.izettle.app.resources;

import com.izettle.app.api.*;
import com.izettle.app.db.*;
import io.dropwizard.testing.junit.*;
import org.junit.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RegisterUserResourceTest {

	private static final UserDAO userDAO = mock(UserDAO.class);

	@ClassRule
	public static final ResourceTestRule resources = ResourceTestRule.builder()
	                                                                 .addResource(new RegisterUserResource(userDAO))
	                                                                 .build();

	@After
	public void tearDown() throws Exception {
		reset(userDAO);
	}

	@Test
	public void execute() throws Exception {
		when(userDAO.findUserByUsername(anyString())).thenReturn(null);
		when(userDAO.createNewUser(anyString(), anyString())).thenReturn(1L);
		StandardResult result = resources.client()
		                                 .target("/register")
		                                 .queryParam("username", "antonio")
		                                 .queryParam("password", "ant0ni0")
		                                 .request()
		                                 .post(null)
		                                 .readEntity(StandardResult.class);
		assertThat(result.isSuccessful()).isTrue();
		assertThat(result.getId()).isEqualTo(1L);
	}
}