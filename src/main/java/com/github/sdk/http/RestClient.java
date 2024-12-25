package com.github.sdk.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.rxjava3.core.Single;
import jakarta.inject.Inject;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class RestClient {

	@Inject
	public OkHttpClient okHttpClient;

	@Inject
	public ObjectMapper objectMapper;

	private final String baseUrl;

	@Inject
	public RestClient(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public <T> Single<Response<T>> get(String url, Class<T> clazz) {
		return createRequest("GET", url, clazz);
	}

	public <T> Single<Response<T>> createRequest(String method, String url, Class<T> clazz) {
		String apiUrl = "%s%s".formatted(baseUrl, url);

		Request request = new Request.Builder().url(apiUrl).method(method, null).build();

		return Single.create(emitter -> {
			this.okHttpClient.newCall(request)
				.enqueue(new RestCallback<>(emitter, this.objectMapper, clazz));
		});
	}
}

