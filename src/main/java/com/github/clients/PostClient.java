package com.github.clients;

import com.github.core.http.Response;
import com.github.core.http.RestClient;
import com.github.model.responses.PostResponse;
import io.reactivex.rxjava3.core.Single;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.List;

/**
 * The type Post client.
 */
@Singleton
public class PostClient {

	private final String baseUrl;

	/**
	 * The Rest client.
	 */
	@Inject
	RestClient restClient;

	/**
	 * Instantiates a new Post client.
	 */
	@Inject
	public PostClient() {
		this.baseUrl = "https://gorest.co.in/public/v2";
	}

	/**
	 * Gets post by user id.
	 *
	 * @param userId the user id
	 * @return the post by user id
	 */
	public Single<List<PostResponse>> getPostByUserId(long userId) {
		String apiUrl = "%s/users/%d/posts".formatted(this.baseUrl, userId);

		return this.restClient
			.getSingle(apiUrl, PostResponse[].class)
			.doOnSuccess(Response::verifyOkOrFail)
			.map(response -> List.of(response.getData()));
	}
}
