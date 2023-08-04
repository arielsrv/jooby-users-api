package app.extensions;

import static com.google.inject.Guice.createInjector;
import static io.jooby.rxjava3.Reactivex.rx;

import app.modules.AppModule;
import com.google.inject.Injector;
import io.jooby.Extension;
import io.jooby.Jooby;
import io.jooby.OpenAPIModule;
import io.jooby.guice.GuiceModule;
import io.jooby.jackson.JacksonModule;
import io.jooby.netty.NettyServer;
import org.jetbrains.annotations.NotNull;

public class Api implements Extension {

    private final Injector injector;

    public Api() {
        this.injector = createInjector(new AppModule());
    }

    @Override
    public boolean lateinit() {
        return Extension.super.lateinit();
    }

    @Override
    public void install(@NotNull Jooby application) {
        application.use(rx());
        application.install(this.injector.getInstance(GuiceModule.class));
        application.install(this.injector.getInstance(JacksonModule.class));
        application.install(this.injector.getInstance(NettyServer.class));
        application.install(this.injector.getInstance(OpenAPIModule.class));
    }
}
