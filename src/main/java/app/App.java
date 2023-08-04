package app;

import app.extensions.Api;
import app.services.UserService;
import io.jooby.Jooby;

public class App extends Jooby {


    {
        install(new Api());
        get("/users", ctx -> {
            UserService userService = require(UserService.class);
            return userService.GetUsers();
        });
    }

    public static void main(final String[] args) {
        runApp(args, App::new);
    }

}
