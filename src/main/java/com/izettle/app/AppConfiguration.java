package com.izettle.app;

import com.fasterxml.jackson.annotation.*;
import io.dropwizard.Configuration;
import io.dropwizard.db.*;
import org.hibernate.validator.constraints.*;

import javax.validation.*;
import javax.validation.constraints.*;

public class AppConfiguration extends Configuration {

	@NotEmpty
	private String sessionTimeoutInSeconds;

	@Valid
	@NotNull
	private DataSourceFactory database = new DataSourceFactory();

	@JsonProperty
	public String getSessionTimeoutInSeconds() {
		return sessionTimeoutInSeconds;
	}

	@JsonProperty
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
}