package com.github.core.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.rxjava3.core.Single;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

@Singleton
public class RestClient {

	@Inject
	public OkHttpClient okHttpClient;

	@Inject
	public ObjectMapper objectMapper;

	public <T> Single<Response<T>> get(String url, Class<T> clazz) {
		return makeRequest("GET", url, clazz);
	}

	public <T> Single<Response<T>> makeRequest(String method, String url, Class<T> clazz) {
		Request request = new Request.Builder().url(url).method(method, null).build();
		return Single.create(emitter -> {
			Call call = this.okHttpClient.newCall(request);
			RESTCallback<T> callback = new RESTCallback<>(emitter, this.objectMapper, clazz);
			call.enqueue(callback);
		});
	}
}

