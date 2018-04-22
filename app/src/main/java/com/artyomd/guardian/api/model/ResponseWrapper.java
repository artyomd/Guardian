package com.artyomd.guardian.api.model;

import com.google.gson.annotations.SerializedName;

public class ResponseWrapper<T> {
	@SerializedName("response")
	private T response;

	public T getResponse() {
		return response;
	}
}
