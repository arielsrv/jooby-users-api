package com.github.controllers;

import com.github.model.UserDto;
import com.github.services.UserService;
import io.jooby.Context;
import io.reactivex.rxjava3.core.Single;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.List;

@Singleton
public class UserController {

	@Inject
	public UserService userService;

	public Single<List<UserDto>> GetUsers(Context ignoredContext) {
		return this.userService.
			getUsers();
	}
}
