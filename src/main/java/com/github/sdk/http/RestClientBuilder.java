package com.github.sdk.http;

public class RestClientBuilder {

	private String baseUrl;
	private long connectionTimeout;
	private long callTimeout;

	public static RestClientBuilder newBuilder() {
		return new RestClientBuilder();
	}

	public RestClientBuilder setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
		return this;
	}

	public RestClientBuilder setConnectionTimeout(long connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
		return this;
	}

	public RestClientBuilder setCallTimeout(long callTimeout) {
		this.callTimeout = callTimeout;
		return this;
	}

	public RestClient build() {
		return new RestClient(this.baseUrl, this.connectionTimeout, this.callTimeout);
	}
}
