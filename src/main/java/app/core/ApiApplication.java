package app.core;

import static com.google.inject.Guice.createInjector;
import static io.jooby.rxjava3.Reactivex.rx;

import app.core.modules.AppModule;
import app.core.modules.PrometheusModule;
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

    protected final Injector injector;

    protected ApiApplication() {
        this.injector = createInjector(new AppModule());
        this.coreSettings();
        this.RegisterExtensions();
        this.RegisterRoutes();
    }

    private void coreSettings() {
        createApp(new String[]{}, ExecutionMode.EVENT_LOOP, () -> this);
        this.use(rx());
        this.RegisterServer();
    }

    private void RegisterServer() {
        this.install(Resolve(NettyServer.class));
    }

    protected void RegisterExtensions() {
        this.install(Resolve(GuiceModule.class));
        this.install(Resolve(JacksonModule.class));
        this.install(Resolve(OpenAPIModule.class));
        this.install(Resolve(PrometheusModule.class));
    }

    protected <T> T Resolve(Class<T> type) {
        return this.injector.getInstance(type);
    }

    protected final <TController, TResult> void Add(String verb, String path,
        Class<TController> type, BiFunction<Context, TController, TResult> action) {
        TController controller = Resolve(type);
        this.route(verb, path, ctx -> action.apply(ctx, controller));
    }

    protected abstract void RegisterRoutes();
}
