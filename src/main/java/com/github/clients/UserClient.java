package com.github.clients;

import com.github.model.requests.CreateUserRequest;
import com.github.model.responses.UserResponse;
import com.github.sdk.http.Response;
import com.github.sdk.http.RestClient;
import io.reactivex.rxjava3.core.Single;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import java.util.List;
import java.util.Map;

@Singleton
public class UserClient {

	private final RestClient restClient;

	@Inject
	public UserClient(@Named("gorest-client") RestClient restClient) {
		this.restClient = restClient;
	}

	public Single<List<UserResponse>> getUsers() {
		String apiUrl = "/users";

		return this.restClient
			.get(apiUrl, UserResponse[].class)
			.doOnSuccess(Response::verifyOkOrFail)
			.map(response -> List.of(response.getData()));
	}

	public Single<Long> createUser(CreateUserRequest createUserRequest) {
		String apiUrl = "/users";
		Map<String, String> headers = Map.of("Authorization",
			"Bearer xxx");

		return this.restClient
			.createRequest("POST", apiUrl, createUserRequest, headers, UserResponse.class)
			.doOnSuccess(Response::verifyOkOrFail)
			.map(response -> response.getData().id);
	}
}
