package com.github.services;

import com.github.clients.PostsClient;
import com.github.clients.TodosClient;
import com.github.clients.UserClient;
import com.github.model.PostDto;
import com.github.model.TodoDto;
import com.github.model.UserDto;
import com.github.model.responses.PostResponse;
import com.github.model.responses.TodoResponse;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Singleton
public class UserService {

	@Inject
	public UserClient userClient;

	@Inject
	public PostsClient postsClient;

	@Inject
	public TodosClient todosClient;

	public Cache<Long, List<PostDto>> appCache = CacheBuilder.newBuilder()
		.expireAfterWrite(1, TimeUnit.MINUTES).concurrencyLevel(4).maximumSize(50).recordStats()
		.build();

	public Single<List<UserDto>> getUsers() {
		return this.userClient.getUsers()
			.flatMap(users -> Observable.fromIterable(users)
				.flatMapSingle(user -> {
					UserDto userDto = new UserDto();
					userDto.id = user.id;
					userDto.name = user.name;
					userDto.email = user.email;
					userDto.posts = new ArrayList<>();
					userDto.todos = new ArrayList<>();

					return Single.zip(
						this.postsClient.getPosts(user.id),
						this.todosClient.getTodos(user.id),
						(postsResponse, todosResponse) -> {
							for (TodoResponse todoResponse : todosResponse) {
								TodoDto todoDto = new TodoDto();
								todoDto.id = todoResponse.id;
								todoDto.title = todoResponse.title;
								todoDto.status = todoResponse.status;
								todoDto.dueOn = todoResponse.dueOn;
								userDto.todos.add(todoDto);
							}

							for (PostResponse postResponse : postsResponse) {
								PostDto postDto = new PostDto();
								postDto.id = postResponse.id;
								postDto.title = postResponse.title;
								postDto.body = postResponse.body;
								userDto.posts.add(postDto);
							}

							return userDto;
						});
				}).toList());
	}
}
