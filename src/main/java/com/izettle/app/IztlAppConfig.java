package com.izettle.app;

import com.fasterxml.jackson.annotation.*;
import com.izettle.app.auth.*;
import io.dropwizard.Configuration;
import io.dropwizard.db.*;
import org.hibernate.validator.constraints.*;

import javax.validation.*;
import javax.validation.constraints.*;
import java.util.*;
import java.util.stream.*;

public class IztlAppConfig extends Configuration {

	@NotEmpty
	private String sessionTimeoutInSeconds;

	@Valid
	@NotNull
	private DataSourceFactory database = new DataSourceFactory();

	@NotNull
	private List<PrincipalUser> authorizedUsers = Collections.EMPTY_LIST;

	@JsonProperty("sessionTimeoutInSeconds")
	public String getSessionTimeoutInSeconds() {
		return sessionTimeoutInSeconds;
	}

	@JsonProperty("sessionTimeoutInSeconds")
	public void setSessionTimeoutInSeconds(String sessionTimeoutInSeconds) {
		this.sessionTimeoutInSeconds = sessionTimeoutInSeconds;
	}

	@JsonProperty("database")
	public DataSourceFactory getDataSourceFactory() {
		return database;
	}

	@JsonProperty("database")
	public void setDataSourceFactory(DataSourceFactory factory) {
		this.database = factory;
	}

	@JsonProperty("authorizedUsers")
	public List<PrincipalUser> getAuthorizedUsers() {
		return authorizedUsers;
	}

	@JsonProperty("authorizedUsers")
	public void setAuthorizedUsers(List<Object> authorizedUsers) {
		this.authorizedUsers = authorizedUsers.stream().map(PrincipalUser::new).collect(Collectors.toList());
	}
}