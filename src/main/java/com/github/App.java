package com.github;

import com.github.controllers.UserController;
import com.github.core.ApiApplication;
import io.jooby.Environment;
import io.jooby.ExecutionMode;

/** The type App. */
public class App extends ApiApplication {

    /**
     * Main.
     *
     * @param args the args
     */
    public static void main(final String[] args) {
		runApp(args, ExecutionMode.EVENT_LOOP, App::new);
    }

    @Override
    public void registerRoutes() {
        add(GET, "/users", UserController.class, (context, userController) -> userController.getUsers(context));
    }
}
