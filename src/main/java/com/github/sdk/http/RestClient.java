package com.github.sdk.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.rxjava3.core.Single;
import jakarta.inject.Inject;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;

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

	public <B, T> Single<Response<T>> post(String url, B body, Class<T> clazz) {
		return createRequest("POST", url, body, clazz);
	}

	public <T> Single<Response<T>> createRequest(String method, String url,
		Class<T> clazz) {
		return createRequest(method, url, null, clazz);
	}

	public <B, T> Single<Response<T>> createRequest(String method, String url, B body,
		Class<T> clazz) {
		String apiUrl = "%s%s".formatted(this.baseUrl, url);

		RequestBody requestBody = null;
		if (body != null) {
			try {
				String json = this.objectMapper.writeValueAsString(body);
				requestBody = RequestBody.create(json, MediaType.parse("application/json"));
			} catch (Exception e) {
				return Single.error(
					new IllegalArgumentException("error serializing request body", e));
			}
		}

		Request request = new Builder().url(apiUrl).method(method, requestBody).build();

		return Single.create(emitter -> {
			this.okHttpClient.newCall(request)
				.enqueue(new RestCallback<>(emitter, this.objectMapper, clazz));
		});
	}
}

