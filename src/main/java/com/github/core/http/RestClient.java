package com.github.core.http;

import static java.util.Objects.requireNonNull;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Singleton
public class RestClient {

	private final static Logger logger = LoggerFactory.getLogger(RestClient.class);

	@Inject
	public OkHttpClient okHttpClient;
	@Inject
	public ObjectMapper objectMapper;


	public <T> Single<Response<T>> GetSingle(String url, Class<T> clazz) {
		Request request = new Request.Builder().url(url).get().build();
		return doRequest(url, request, clazz);
	}

	public <T> Single<Response<T>> doRequest(String url, Request request, Class<T> clazz) {
		return Single.create(emitter -> {

			logger.debug("GET / %s".formatted(url));
			this.okHttpClient.newCall(request).enqueue(new Callback() {
				@Override
				public void onFailure(@NotNull Call call, @NotNull IOException e) {
					logger.error(e.getMessage());
					emitter.onError(e);
				}

				@Override
				public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) {
					try {
						T result = null;
						if (response.isSuccessful()) {
							result = objectMapper.readValue(
								requireNonNull(response.body()).string(), clazz);
						}
						emitter.onSuccess(
							new Response<>(response.code(), response.headers(), response.body(),
								result));
					} catch (Exception e) {
						logger.error(e.getMessage());
						emitter.onError(e);
					}
				}
			});
		});
	}
}
