package app.core.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
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

    public <T> Single<T> GetSingle(String url, Class<T> clazz) {
        logger.debug("request to: %s".formatted(url));
        return Single.create(emitter -> {
            Request request = new Request.Builder().url(url).build();

            this.okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    logger.error(e.getMessage());
                    emitter.onError(e);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response)
                    throws IOException {
                    if (!response.isSuccessful()) {
                        IOException e = new IOException("Unexpected code " + response);
                        logger.error(e.getMessage());
                        emitter.onError(e);
                        return;
                    }

                    T result = objectMapper.readValue(
                        Objects.requireNonNull(response.body()).string(), clazz);
                    emitter.onSuccess(result);
                }
            });
        });
    }

    public <T> Flowable<T> GetFlowable(String url, Class<T> clazz) {
        return Flowable.create(emitter -> {
            Request request = new Request.Builder().url(url).build();
            logger.debug("request to: %s".formatted(url));

            this.okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    logger.error(e.getMessage());
                    emitter.onError(e);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response)
                    throws IOException {
                    if (!response.isSuccessful()) {
                        IOException e = new IOException("Unexpected code " + response);
                        logger.error(e.getMessage());
                        emitter.onError(e);
                        return;
                    }

                    List<T> result = objectMapper.readValue(
                        Objects.requireNonNull(response.body()).string(),
                        objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));

                    for (T element : result) {
                        emitter.onNext(element);
                    }

                    emitter.onComplete();
                }
            });
        }, BackpressureStrategy.ERROR);
    }
}
