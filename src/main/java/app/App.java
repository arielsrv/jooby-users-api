package app;

import app.extensions.Api;
import app.services.UserService;
import io.jooby.Jooby;

public class App extends Jooby {

    {
        install(new Api());

        get("/", ctx -> "Welcome to Jooby!");
        get("/users", ctx -> {
            UserService service = require(UserService.class);
            return service.GetUsers();
        });
    }

    public static void main(final String[] args) {
        runApp(args, App::new);
    }

}
