package com.izettle.app.api;

import com.google.common.base.*;

public class StandardResult {

	private Long id;
	private Boolean successful;

	public StandardResult() {}

	public StandardResult(Long id, Boolean successful) {
		this.id = id;
		this.successful = successful;
	}

	public Long getId() {
		return id;
	}

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