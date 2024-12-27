package com.github.controllers;

import com.github.model.CreateUserDto;
import com.github.model.UserDto;
import com.github.sdk.ApiContext;
import com.github.sdk.ApiController;
import com.github.services.UserService;
import io.reactivex.rxjava3.core.Single;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.List;

@Singleton
public class UserController extends ApiController {

	@Inject
	public UserService userService;

	public Single<List<UserDto>> getUsers(ApiContext ignoredCtx) {
		return this.userService.getUsers();
	}

	public Single<Long> createUser(ApiContext ctx) {
		CreateUserDto createUserDto = ctx.body().to(CreateUserDto.class);
		return this.userService.createUser(createUserDto);
	}
}
