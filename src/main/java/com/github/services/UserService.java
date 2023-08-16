package com.github.services;

import com.github.clients.PostClient;
import com.github.clients.UserClient;
import com.github.model.PostDto;
import com.github.model.UserDto;
import com.github.model.responses.PostResponse;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class UserService {

	@Inject
	public UserClient userClient;

	@Inject
	public PostClient postClient;

	public Flowable<UserDto> GetUsers() {
		return this.userClient.GetUsers().flatMap(userResponse -> {
			UserDto userDto = new UserDto();
			userDto.id = userResponse.id;
			userDto.name = userResponse.name;
			userDto.email = userResponse.email;

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

					return Single.just(userDto);
				})
				.toFlowable();
		});
	}
}
