package com.github.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.github.clients.PostsClient;
import com.github.clients.TodosClient;
import com.github.clients.UserClient;
import com.github.model.UserDto;
import com.github.model.responses.PostResponse;
import com.github.model.responses.TodoResponse;
import com.github.model.responses.UserResponse;
import io.reactivex.rxjava3.core.Single;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserServiceUnitTest {

	private UserClient userClient;
	private PostsClient postsClient;
	private TodosClient todosClient;

	@BeforeEach
	public void setUp() {
		this.userClient = mock(UserClient.class);
		this.postsClient = mock(PostsClient.class);
		this.todosClient = mock(TodosClient.class);
	}

	@Test
	public void get_Users() {
		when(this.userClient.getUsers()).thenReturn(getUsers());
		when(this.postsClient.getPosts(anyLong())).thenReturn(getPosts());
		when(this.todosClient.getTodos(anyLong())).thenReturn(getTodos());

		UserService userService = new UserService();
		userService.userClient = this.userClient;
		userService.postsClient = this.postsClient;
		userService.todosClient = this.todosClient;

		List<UserDto> actual = userService.getUsers().blockingGet();
		assertNotNull(actual);
		assertEquals(1, actual.size());
	}

	private Single<List<TodoResponse>> getTodos() {
		TodoResponse todoResponse = new TodoResponse();
		todoResponse.id = 1;
		todoResponse.title = "title";

		return Single.just(List.of(todoResponse));
	}


	private Single<List<UserResponse>> getUsers() {
		UserResponse userResponse = new UserResponse();
		userResponse.id = 1L;
		userResponse.name = "John Doe";

		return Single.just(List.of(userResponse));
	}

	private Single<List<PostResponse>> getPosts() {
		PostResponse postResponse = new PostResponse();
		postResponse.id = 1;
		postResponse.title = "title";

		return Single.just(List.of(postResponse));
	}
}
