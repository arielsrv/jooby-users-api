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
		.expireAfterWrite(1, TimeUnit.MINUTES).concurrencyLevel(4).maximumSize(50).recordStats()
		.build();

	public Single<List<UserDto>> getUsers() {
		return this.userClient.getUsers().flatMapObservable(Observable::fromIterable)
			.flatMapSingle(userResponse -> {
				UserDto userDto = new UserDto();
				userDto.id = userResponse.id;
				userDto.name = userResponse.name;
				userDto.email = userResponse.email;

				List<PostDto> cachedPosts = this.appCache.getIfPresent(userDto.id);
				if (cachedPosts != null) {
					userDto.posts = cachedPosts;
					return Single.just(userDto);
				}

				return this.postClient.getPosts(userDto.id).map(postsResponse -> {
					List<PostDto> postDtos = new ArrayList<>();
					for (PostResponse postResponse : postsResponse) {
						PostDto postDto = new PostDto();
						postDto.id = postResponse.id;
						postDto.title = postResponse.title;
						postDto.body = postResponse.body;
						postDtos.add(postDto);
					}
					return postDtos;
				}).doOnSuccess(posts -> {
					this.appCache.put(userDto.id, posts);
				}).map(posts -> {
					userDto.posts = posts;
					return userDto;
				});
			}).toList();
	}

}
