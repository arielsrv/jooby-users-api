package com.github.clients;

import com.github.model.responses.PostResponse;
import com.github.sdk.http.Response;
import com.github.sdk.http.RestClient;
import io.reactivex.rxjava3.core.Single;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.List;

@Singleton
public class PostClient {

	private final String baseUrl;

	@Inject
	public RestClient restClient;

	@Inject
	public PostClient() {
		this.baseUrl = "https://gorest.co.in/public/v2";
	}

	public Single<List<PostResponse>> getPosts(long userId) {
		String apiUrl = "%s/users/%d/posts".formatted(this.baseUrl, userId);

		return this.restClient
			.get(apiUrl, PostResponse[].class)
			.doOnSuccess(Response::verifyOkOrFail)
			.map(response -> List.of(response.getData()));
	}
}
