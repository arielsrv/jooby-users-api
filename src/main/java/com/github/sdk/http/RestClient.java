package com.github.sdk.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.rxjava3.core.Single;
import jakarta.inject.Inject;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import org.apache.commons.validator.routines.UrlValidator;

public class RestClient {

	private final String baseUrl;

	public OkHttpClient okHttpClient;
	@Inject
	public ObjectMapper objectMapper;
	@Inject
	public UrlValidator urlValidator;

	@Inject
	public RestClient(String baseUrl, long connectTimeout, long callTimeout) {
		this.baseUrl = baseUrl;
		this.okHttpClient = new OkHttpClient().newBuilder()
			.connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
			.callTimeout(callTimeout, TimeUnit.MILLISECONDS).build();
	}

	public <T> Single<Response<T>> get(String url, Class<T> clazz) {
		return createRequest("GET", url, null, null, clazz);
	}

	public <T> Single<Response<T>> get(String url, Map<String, String> headers, Class<T> clazz) {
		return createRequest("GET", url, null, headers, clazz);
	}

	public <B, T> Single<Response<T>> post(String url, B body, Class<T> clazz) {
		return createRequest("POST", url, null, null, clazz);
	}

	public <B, T> Single<Response<T>> post(String url, B body, Map<String, String> headers,
		Class<T> clazz) {
		return createRequest("POST", url, body, headers, clazz);
	}

	public <B, T> Single<Response<T>> createRequest(String method, String url, B body,
		Map<String, String> headers, Class<T> clazz) {

		String apiUrl = "%s%s".formatted(this.baseUrl, url);
		if (!this.urlValidator.isValid(apiUrl)) {
			return Single.error(
				new IllegalArgumentException("invalid API URL: %s".formatted(apiUrl)));
		}

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

		Builder builder = new Builder().url(apiUrl).method(method, requestBody);
		if (headers != null) {
			for (Entry<String, String> entry : headers.entrySet()) {
				builder = builder.addHeader(entry.getKey(), entry.getValue());
			}
		}

		Builder finalBuilder = builder;
		return Single.create(emitter -> {
			this.okHttpClient.newCall(finalBuilder.build())
				.enqueue(new RestCallback<>(emitter, this.objectMapper, clazz));
		});
	}
}

