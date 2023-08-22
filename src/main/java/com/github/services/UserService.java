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
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class UserService {

	private final static Logger logger = LoggerFactory.getLogger(UserService.class);
	private final Cache<Long, List<PostDto>> postDtoCache = CacheBuilder.newBuilder()
		.expireAfterWrite(1, TimeUnit.MINUTES)
		.concurrencyLevel(Runtime.getRuntime().availableProcessors() - 1)
		.maximumSize(50)
		.recordStats()
		.build();
	@Inject
	public UserClient userClient;
	@Inject
	public PostClient postClient;

	public Single<List<UserDto>> GetUsers() {
		return this.userClient.GetUsers().flatMapObservable(Observable::fromIterable)
			.flatMapSingle(userResponse -> {
				UserDto userDto = new UserDto();
				userDto.id = userResponse.id;
				userDto.name = userResponse.name;
				userDto.email = userResponse.email;

				List<PostDto> postsDto = this.postDtoCache.getIfPresent(userDto.id);
				if (postsDto != null) {
					userDto.posts = postsDto;
					return Single.just(userDto);
				} else {
					logger.debug("cache miss key %d".formatted(userDto.id));
					return getFromApi(userDto);
				}

			}).collect(ArrayList::new, List::add);
	}

	@NotNull
	private Single<UserDto> getFromApi(UserDto userDto) {
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
			// only for poc/test
			this.postDtoCache.put(userDto.id, userDto.posts);
			return Single.just(userDto);
		});
	}
}
