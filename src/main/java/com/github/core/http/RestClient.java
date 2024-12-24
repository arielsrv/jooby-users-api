package com.github.core.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.rxjava3.core.Single;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.jetbrains.annotations.NotNull;

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
			call.enqueue(new Callback() {
				@Override
				public void onFailure(@NotNull Call call, @NotNull IOException e) {
					emitterOnError(e);
				}

				private void emitterOnError(Exception e) {
					if (!emitter.isDisposed()) {
						emitter.onError(e);
					}
				}

				@Override
				public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) {
					try {
						if (!response.isSuccessful()) {
							throw new IOException("Unexpected HTTP response: " + response.code());
						}

						T unmarshal;
						try (okhttp3.ResponseBody responseBody = response.body()) {
							if (responseBody == null) {
								throw new IOException("Response body is null");
							}
							unmarshal = objectMapper.readValue(responseBody.string(), clazz);
						}

						if (!emitter.isDisposed()) {
							emitter.onSuccess(new Response<>(response.code(), unmarshal));
						}
					} catch (Exception e) {
						emitterOnError(e);
					}
				}
			});
		});
	}
}
