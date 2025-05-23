package com.github.sdk.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.rxjava3.core.SingleEmitter;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import org.jetbrains.annotations.NotNull;

public class RestCallback<T> implements Callback {

	private final SingleEmitter<Response<T>> emitter;
	private final ObjectMapper objectMapper;
	private final Class<T> clazz;


	public RestCallback(
		SingleEmitter<Response<T>> emitter,
		ObjectMapper objectMapper,
		Class<T> clazz
	) {
		this.emitter = emitter;
		this.objectMapper = objectMapper;
		this.clazz = clazz;
	}

	@Override
	public void onFailure(@NotNull Call call, @NotNull IOException e) {
		this.emitterOnError(e);
	}

	@Override
	public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) {
		try {
			if (!response.isSuccessful()) {
				throw new IOException("Unexpected HTTP response: " + response.code());
			}

			T result;
			try (okhttp3.ResponseBody responseBody = response.body()) {
				if (responseBody == null) {
					throw new IOException("Response body is null");
				}
				result = this.objectMapper.readValue(responseBody.string(), this.clazz);
			}

			if (!this.emitter.isDisposed()) {
				this.emitter.onSuccess(new Response<>(response.code(), result));
			}
		} catch (Exception e) {
			this.emitterOnError(e);
		}
	}


	private void emitterOnError(Exception e) {
		if (!this.emitter.isDisposed()) {
			this.emitter.onError(e);
		}
	}
}
