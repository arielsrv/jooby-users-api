package com.github.core.http;

import java.io.IOException;

public class Response<T> {

	private final int code;
	private final T data;


	public Response(int code, T data) {
		this.code = code;
		this.data = data;
	}

	public void VerifyOkOrFail() throws IOException {
		if ((200 <= this.code) && (this.code <= 299)) {
			return;
		}
		throw new IOException();
	}

	public T GetData() {
		return this.data;
	}
}
