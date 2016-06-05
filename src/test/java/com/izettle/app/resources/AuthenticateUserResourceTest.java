package com.izettle.app.resources;

import com.izettle.app.api.*;
import com.izettle.app.core.*;
import com.izettle.app.db.*;
import io.dropwizard.testing.junit.*;
import org.junit.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Created by antonio on 05/06/16.
 */
public class AuthenticateUserResourceTest {

	private static final UserDAO userDAO = mock(UserDAO.class);
	private static final UserSessionDAO userSessionDAO = mock(UserSessionDAO.class);

	private User user;

	@ClassRule
	public static final ResourceTestRule resources =
			ResourceTestRule.builder()
			                .addResource(new AuthenticateUserResource(userDAO, userSessionDAO, 10))
			                .build();

	@Before
	public void setUp() {
		user = new User(1L, "antonio", "$31$16$s4pZlvrjhfNBtA6OAML8Q_g9wgE5wWp1hSIu2AgI44A");
	}

	@After
	public void tearDown() throws Exception {
		reset(userDAO);
		reset(userSessionDAO);
	}

	@Test
	public void execute() throws Exception {
		when(userDAO.findUserByUsername(anyString())).thenReturn(user);
		when(userSessionDAO.findIdByUserIdAndThreshold(anyLong(), any())).thenReturn(null);
		when(userSessionDAO.createUserSessionForUserId(user.getId())).thenReturn(1L);
		AuthenticationResult result = resources.client()
		                                       .target("/authenticate")
		                                       .queryParam("username", "antonio")
		                                       .queryParam("password", "ant0ni0")
		                                       .request()
		                                       .post(null)
		                                       .readEntity(AuthenticationResult.class);
		assertThat(result.isSuccessful()).isTrue();
		assertThat(result.getId()).isEqualTo(1L);
		assertThat(result.getSessionId()).isEqualTo(1L);
	}
}