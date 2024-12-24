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

/**
 * The type Rest client.
 */
@Singleton
public class RestClient {

	private static final Logger logger = LoggerFactory.getLogger(RestClient.class);

	/**
	 * The Ok http client.
	 */
	@Inject
	public OkHttpClient okHttpClient;

	/**
	 * The Object mapper.
	 */
	@Inject
	public ObjectMapper objectMapper;

	/**
	 * Gets single.
	 *
	 * @param <T>   the type parameter
	 * @param url   the url
	 * @param clazz the clazz
	 * @return the single
	 */
	public <T> Single<Response<T>> getSingle(String url, Class<T> clazz) {
		Request request = new Request.Builder().url(url).get().build();
		return doRequest(url, request, clazz);
	}

	/**
	 * Do request single.
	 *
	 * @param <T>     the type parameter
	 * @param url     the url
	 * @param request the request
	 * @param clazz   the clazz
	 * @return the single
	 */
	public <T> Single<Response<T>> doRequest(String url, Request request, Class<T> clazz) {
		return Single.create(emitter -> {
			this.okHttpClient.newCall(request).enqueue(new Callback() {
				@Override
				public void onFailure(@NotNull Call call, @NotNull IOException e) {
					logger.error(e.getMessage());
					emitter.onError(e);
				}

				@Override
				public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) {
					try {
						T data = null;
						if (response.isSuccessful()) {
							data = objectMapper.readValue(requireNonNull(response.body()).string(),
								clazz);
						}
						emitter.onSuccess(new Response<>(response.code(), data));
					} catch (Exception e) {
						logger.error(e.getMessage());
						emitter.onError(e);
					}
				}
			});
		});
	}
}
