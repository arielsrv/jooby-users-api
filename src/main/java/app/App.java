package app;

import app.extensions.Api;
import app.extensions.Routes;
import io.jooby.Jooby;

public class App extends Jooby {

    {
        install(new Api());
        install(new Routes());
    }

    public static void main(final String[] args) {
        runApp(args, App::new);
    }

}
