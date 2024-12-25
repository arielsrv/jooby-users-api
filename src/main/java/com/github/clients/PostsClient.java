package com.github.clients;

import com.github.model.responses.PostResponse;
import com.github.sdk.http.Response;
import com.github.sdk.http.RestClient;
import io.reactivex.rxjava3.core.Single;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import java.util.List;

@Singleton
public class PostsClient {

	private final RestClient restClient;

	@Inject
	public PostsClient(@Named("posts-client") RestClient restClient) {
		this.restClient = restClient;
	}

	public Single<List<PostResponse>> getPosts(long userId) {
		String apiUrl = "/users/%d/posts".formatted(userId);

		return this.restClient.get(apiUrl, PostResponse[].class)
			.doOnSuccess(Response::verifyOkOrFail).map(response -> List.of(response.getData()));
	}
}
