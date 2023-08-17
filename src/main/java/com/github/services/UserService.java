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

	private final Cache<Long, List<PostDto>> postDtoCache = CacheBuilder.newBuilder()
		.expireAfterWrite(1, TimeUnit.MINUTES).concurrencyLevel(getConcurrencyLevel())
		.maximumSize(50).recordStats().build();

	private static int getConcurrencyLevel() {
		return Runtime.getRuntime().availableProcessors() - 1;
	}

	public Single<List<UserDto>> GetUsers() {
		return this.userClient.GetUsers().flatMapObservable(Observable::fromIterable)
			.flatMapSingle(userResponse -> {
				UserDto userDto = new UserDto();
				userDto.id = userResponse.id;
				userDto.name = userResponse.name;
				userDto.email = userResponse.email;

				// guava poc, remove it
				List<PostDto> postsDto = this.postDtoCache.getIfPresent(userDto.id);

				if (postsDto != null) {
					userDto.posts = postsDto;
					return Single.just(userDto);
				}

				return this.postClient.GetPostByUserId(userDto.id).flatMap(postsResponse -> {
					List<PostDto> postDtos = new ArrayList<>();
					for (PostResponse postResponse : postsResponse) {
						PostDto postDto = new PostDto();
						postDto.id = postResponse.id;
						postDto.title = postResponse.title;
						postDto.body = postResponse.body;
						postDtos.add(postDto);
					}
					userDto.posts = postDtos;
					this.postDtoCache.put(userDto.id, postDtos);
					return Single.just(userDto);
				});
			}).collect(ArrayList::new, List::add);
	}
}
