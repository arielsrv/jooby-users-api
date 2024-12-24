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

/**
 * The type User service.
 */
@Singleton
public class UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

	/**
	 * The App cache.
	 */
	public Cache<Long, List<PostDto>> appCache = CacheBuilder.newBuilder()
		.expireAfterWrite(1, TimeUnit.MINUTES)
		.concurrencyLevel(Runtime.getRuntime().availableProcessors() - 1)
		.maximumSize(50)
		.recordStats()
		.build();

	/**
	 * The User client.
	 */
	@Inject
	public UserClient userClient;

	/**
	 * The Post client.
	 */
	@Inject
	public PostClient postClient;

	/**
	 * Gets users.
	 *
	 * @return the users
	 */
	public Single<List<UserDto>> getUsers() {
		return this.userClient
			.getUsers()
			.flatMapObservable(Observable::fromIterable)
			.flatMapSingle(userResponse -> {
				UserDto userDto = new UserDto();
				userDto.id = userResponse.id;
				userDto.name = userResponse.name;
				userDto.email = userResponse.email;

				userDto.posts = this.appCache.getIfPresent(userDto.id);
				if (userDto.posts != null) {
					return Single.just(userDto);
				}

				logger.debug("posts from api {}", userDto.id);

				return getFromApi(userDto).flatMap(postResponse -> {
					userDto.posts = postResponse;
					this.appCache.put(userDto.id, userDto.posts);
					return Single.just(userDto);
				});
			})
			.collect(ArrayList::new, List::add);
	}

	@NotNull
	private Single<List<PostDto>> getFromApi(UserDto userDto) {
		return this.postClient.getPostByUserId(userDto.id).flatMap(postsResponse -> {
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
