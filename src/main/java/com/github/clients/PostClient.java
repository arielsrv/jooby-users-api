package com.github.clients;

import com.github.core.http.Response;
import com.github.core.http.RestClient;
import com.github.model.responses.PostResponse;
import io.reactivex.rxjava3.core.Single;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.List;

@Singleton
public class PostClient {

    private final String baseUrl;
    @Inject
    RestClient restClient;

    @Inject
    public PostClient() {
        this.baseUrl = "https://gorest.co.in/public/v2";
    }

    public Single<List<PostResponse>> GetPostByUserId(long userId) {
        String apiUrl = "%s/users/%d/posts".formatted(this.baseUrl, userId);

        return this.restClient.GetSingle(apiUrl, PostResponse[].class)
            .doOnSuccess(Response::VerifyOkOrFail).map(response -> List.of(response.GetData()));
    }
}
