package com.github.clients;

import com.github.model.responses.UserResponse;
import com.github.sdk.http.Response;
import com.github.sdk.http.RestClient;
import io.reactivex.rxjava3.core.Single;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import java.util.List;

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
}
