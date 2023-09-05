package com.github.clients;

import com.github.core.http.Response;
import com.github.core.http.RestClient;
import com.github.model.responses.UserResponse;
import io.reactivex.rxjava3.core.Single;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.List;


/**
 * The type User client.
 */
@Singleton
public class UserClient {

	private final String baseUrl;
	/**
	 * The Rest client.
	 */
	@Inject
	RestClient restClient;

	/**
	 * Instantiates a new User client.
	 */
	public UserClient() {
		this.baseUrl = "https://gorest.co.in/public/v2";
	}

	/**
	 * Gets users.
	 *
	 * @return the users
	 */
	public Single<List<UserResponse>> getUsers() {
		String apiUrl = "%s/users".formatted(this.baseUrl);

		return this.restClient.getSingle(apiUrl, UserResponse[].class)
			.doOnSuccess(Response::verifyOkOrFail).map(response -> List.of(response.getData()));
	}
}
