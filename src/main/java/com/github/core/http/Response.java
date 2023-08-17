package com.github.core.http;

import java.io.IOException;
import okhttp3.Headers;
import okhttp3.ResponseBody;

public class Response<T> {

	private final int code;
	private final Headers headers;
	private final ResponseBody body;
	private final T data;


	public Response(int code, Headers headers, ResponseBody body, T data) {
		this.code = code;
		this.headers = headers;
		this.body = body;
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
