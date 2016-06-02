package com.izettle.app.api;

import com.fasterxml.jackson.annotation.*;
import com.google.common.base.MoreObjects;

public class Result {

	private Long id;
	private Boolean successful;

	public Result() {}

	public Result(long id, Boolean successful) {
		this.id = id;
		this.successful = successful;
	}

	@JsonProperty
	public long getId() {
		return id;
	}

	@JsonProperty
	public Boolean isSuccessful() {
		return successful;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("id", id)
				.add("successful", successful)
				.toString();
	}
}