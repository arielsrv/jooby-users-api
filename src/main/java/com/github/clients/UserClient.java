package com.github.clients;

import com.github.core.http.Response;
import com.github.core.http.RestClient;
import com.github.model.responses.UserResponse;
import io.reactivex.rxjava3.core.Single;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.List;


@Singleton
public class UserClient {

	private final String baseUrl;
	@Inject
	RestClient restClient;

	public UserClient() {
		this.baseUrl = "https://gorest.co.in/public/v2";
	}

	public Single<List<UserResponse>> getUsers() {
		String apiUrl = "%s/users".formatted(this.baseUrl);

		return this.restClient.getSingle(apiUrl, UserResponse[].class)
			.doOnSuccess(Response::verifyOkOrFail).map(response -> List.of(response.getData()));
	}
}
