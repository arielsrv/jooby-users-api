package app;

import app.modules.ObjectMapperModule;
import app.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.jooby.Jooby;
import io.jooby.OpenAPIModule;
import io.jooby.guice.GuiceModule;
import io.jooby.jackson.JacksonModule;
import io.jooby.netty.NettyServer;
import io.jooby.rxjava3.Reactivex;

public class App extends Jooby {

    {
        use(Reactivex.rx());

        Injector injector = Guice.createInjector(new ObjectMapperModule());

        install(new GuiceModule(injector));
        install(new JacksonModule(injector.getInstance(ObjectMapper.class)));
        install(new NettyServer());
        install(new OpenAPIModule());

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
