package com.github.services;

import com.github.clients.PostClient;
import com.github.clients.UserClient;
import com.github.model.PostDto;
import com.github.model.UserDto;
import com.github.model.responses.PostResponse;
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
	public PostClient postClient;

	public Cache<Long, List<PostDto>> appCache = CacheBuilder.newBuilder()
		.expireAfterWrite(1, TimeUnit.MINUTES)
		.concurrencyLevel(4)
		.maximumSize(50)
		.recordStats()
		.build();

	public Single<List<UserDto>> getUsers() {
		if (this.userClient.restClient == this.postClient.restClient) {
			System.out.println(
				"Using the same RestClient instance for both User and Post clients. This might lead to potential issues.");
		} else {
			System.out.println("Using different RestClient instances for User and Post clients.");
		}
		return this.userClient.getUsers().flatMapObservable(Observable::fromIterable)
			.flatMapSingle(userResponse -> {
				UserDto userDto = new UserDto();
				userDto.id = userResponse.id;
				userDto.name = userResponse.name;
				userDto.email = userResponse.email;

				userDto.posts = this.appCache.getIfPresent(userDto.id);
				if (userDto.posts != null) {
					return Single.just(userDto);
				}

				return getFromApi(userDto).flatMap(postResponse -> {
					userDto.posts = postResponse;
					this.appCache.put(userDto.id, userDto.posts);
					return Single.just(userDto);
				});
			}).collect(ArrayList::new, List::add);
	}

	private Single<List<PostDto>> getFromApi(UserDto userDto) {
		return this.postClient.getPosts(userDto.id).flatMap(postsResponse -> {
			List<PostDto> postDtos = new ArrayList<>();
			for (PostResponse postResponse : postsResponse) {
				PostDto postDto = new PostDto();
				postDto.id = postResponse.id;
				postDto.title = postResponse.title;
				postDto.body = postResponse.body;
				postDtos.add(postDto);
			}

			return Single.just(postDtos);
		});
	}
}
