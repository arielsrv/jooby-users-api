package app;

import static com.google.inject.Guice.createInjector;
import static io.jooby.rxjava3.Reactivex.rx;

import app.modules.AppModule;
import com.google.inject.Injector;
import io.jooby.Context;
import io.jooby.ExecutionMode;
import io.jooby.Jooby;
import io.jooby.OpenAPIModule;
import io.jooby.guice.GuiceModule;
import io.jooby.jackson.JacksonModule;
import io.jooby.netty.NettyServer;
import java.util.function.BiFunction;

public abstract class ApiApplication extends Jooby {

    final Injector injector;

    public ApiApplication() {
        this.injector = createInjector(new AppModule());
        this.use(rx());
        this.install(this.injector.getInstance(GuiceModule.class));
        this.install(this.injector.getInstance(JacksonModule.class));
        this.install(this.injector.getInstance(NettyServer.class));
        this.install(this.injector.getInstance(OpenAPIModule.class));
        RegisterRoutes();
        Jooby.createApp(new String[]{}, ExecutionMode.EVENT_LOOP, () -> this);
    }

    public abstract void RegisterRoutes();

    public <T, R> void Add(String verb, String path, Class<T> type,
        BiFunction<Context, T, R> action) {
        T controller = this.injector.getInstance(type);
        this.route(verb, path, ctx -> action.apply(ctx, controller));
    }
}
