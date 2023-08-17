package com.github;

import com.github.controllers.UserController;
import com.github.core.ApiApplication;
import io.jooby.ExecutionMode;

public class App extends ApiApplication {

	public static void main(final String[] args) {
		runApp(args, ExecutionMode.EVENT_LOOP, App::new);
	}

	@Override
	public void RegisterRoutes() {
		Add(GET, "/users", UserController.class,
			(context, userController) -> userController.GetUsers(context));
	}
}
