package com.izettle.app.resources;

import com.google.common.collect.*;
import com.izettle.app.api.*;
import com.izettle.app.core.*;
import com.izettle.app.db.*;
import io.dropwizard.testing.junit.*;
import org.junit.*;

import java.sql.*;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created by antonio on 05/06/16.
 */
public class ListTimestampsResourceTest {

	private static final UserDAO userDAO = mock(UserDAO.class);
	private static final UserSessionDAO userSessionDAO = mock(UserSessionDAO.class);

	private UserSession userSession;
	private List<UserSession> userSessions;

	@ClassRule
	public static final ResourceTestRule resources =
			ResourceTestRule.builder()
			                .addResource(new ListTimestampsResource(userDAO, userSessionDAO))
			                .build();

	public ListTimestampsResourceTest() {}

	@Before
	public void setUp() {
		userSession = new UserSession(1L, 1L, new Timestamp(System.currentTimeMillis()));
		userSessions = ImmutableList.of(userSession);
	}

	@After
	public void tearDown() throws Exception {
		reset(userDAO);
		reset(userSessionDAO);
	}

	@Test
	public void execute() throws Exception {
		when(userDAO.findIdByUsername(anyString())).thenReturn(1L);
		when(userSessionDAO.findIdByUserId(1L)).thenReturn(1L);
		when(userSessionDAO.findLastUserTimestamps(1L, 5)).thenReturn(userSessions);
		TimestampsResult result = resources.client()
		                                   .target("/listTimestamps")
		                                   .queryParam("username", "antonio")
		                                   .queryParam("sessionId", 1L)
		                                   .request()
		                                   .post(null)
		                                   .readEntity(TimestampsResult.class);
		assertThat(result.isSuccessful()).isTrue();
		assertThat(result.getId()).isEqualTo(1L);
		assertThat(result.getTimestampList().contains(userSession.getTimestamp())).isTrue();
	}

	@Test
	public void executeAdmin() throws Exception {
		when(userDAO.findIdByUsername(anyString())).thenReturn(1L);
		when(userSessionDAO.findLastUserTimestamps(1L, 5)).thenReturn(userSessions);
		TimestampsResult result = resources.client()
		                                   .target("/listTimestamps/admin")
		                                   .queryParam("username", "antonio")
		                                   .request()
		                                   .post(null)
		                                   .readEntity(TimestampsResult.class);
		assertThat(result.isSuccessful()).isTrue();
		assertThat(result.getId()).isEqualTo(1L);
		assertThat(result.getTimestampList().contains(userSession.getTimestamp())).isTrue();
	}
}