package com.github;

import com.github.controllers.UserController;
import com.github.sdk.ApiRoutes;

public class Routes extends ApiRoutes {

	@Override
	public void register() {
		get("/users", UserController.class,
			(context, userController) -> userController.getUsers(context));

		post("/users", UserController.class,
			(context, userController) -> userController.createUser(context));
	}
}
