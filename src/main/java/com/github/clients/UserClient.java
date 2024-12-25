package com.github.clients;

import com.github.model.responses.UserResponse;
import com.github.sdk.http.Response;
import com.github.sdk.http.RestClient;
import io.reactivex.rxjava3.core.Single;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.List;

@Singleton
public class UserClient {

	private final String baseUrl;
	@Inject
	public RestClient restClient;

	public UserClient() {
		this.baseUrl = "https://gorest.co.in/public/v2";
	}

	public Single<List<UserResponse>> getUsers() {
		String apiUrl = "%s/users".formatted(this.baseUrl);

		return this.restClient
			.get(apiUrl, UserResponse[].class)
			.doOnSuccess(Response::verifyOkOrFail)
			.map(response -> List.of(response.getData()));
	}
}
