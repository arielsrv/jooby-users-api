package com.github.core.http;

import static java.util.Objects.requireNonNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.io.IOException;
import java.util.List;
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
        return doSingleRequest(url, request, clazz);
    }

    public <T> Flowable<Response<T>> GetFlowable(String url, Class<T> clazz) {
        Request request = new Request.Builder().url(url).get().build();
        return doFlowableRequest(url, request, clazz);
    }

    public <T> Flowable<Response<T>> doFlowableRequest(String url, Request request,
        Class<T> clazz) {
        return Flowable.create(emitter -> {

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
                        List<T> result = null;
                        if (response.isSuccessful()) {
                            CollectionType collectionType = TypeFactory.defaultInstance()
                                .constructCollectionType(List.class, clazz);

                            String jsonAsString = response.body().string();
                            result = objectMapper.readValue(jsonAsString, collectionType);
                        }

                        for (T element : result) {
                            emitter.onNext(
                                new Response<>(response.code(), response.headers(), response.body(),
                                    element));
                        }

                        emitter.onComplete();

                    } catch (Exception e) {
                        logger.error(e.getMessage());
                        emitter.onError(e);
                    }
                }
            });
        }, BackpressureStrategy.ERROR);
    }

    public <T> Single<Response<T>> doSingleRequest(String url, Request request, Class<T> clazz) {
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
