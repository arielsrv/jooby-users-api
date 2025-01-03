package com.github.clients;

import com.github.model.responses.TodoResponse;
import com.github.sdk.http.Response;
import com.github.sdk.http.RestClient;
import io.reactivex.rxjava3.core.Single;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import java.util.List;

@Singleton
public class TodosClient {

	private final RestClient restClient;

	@Inject
	public TodosClient(@Named("gorest-client") RestClient restClient) {
		this.restClient = restClient;
	}

	public Single<List<TodoResponse>> getTodos(long userId) {
		String apiUrl = "/users/%d/todos".formatted(userId);

		return this.restClient.get(apiUrl, TodoResponse[].class)
			.doOnSuccess(Response::verifyOkOrFail).map(response -> List.of(response.getData()));
	}
}
