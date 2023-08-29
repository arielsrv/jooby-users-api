package com.github.core;

import static com.google.inject.Guice.createInjector;
import static io.jooby.rxjava3.Reactivex.rx;

import com.github.core.modules.AppModule;
import com.github.core.modules.PrometheusModule;
import com.google.inject.Injector;
import io.jooby.Context;
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
		this.registerExtensions();
		this.registerRoutes();
	}

	private void coreSettings() {
		this.use(rx());
		this.registerServer();
	}

	protected void registerServer() {
		this.install(resolve(NettyServer.class));
	}

	protected void registerExtensions() {
		this.install(resolve(GuiceModule.class));
		this.install(resolve(JacksonModule.class));
		this.install(resolve(OpenAPIModule.class));
		this.install(resolve(PrometheusModule.class));
	}

	protected <T> T resolve(Class<T> type) {
		return this.injector.getInstance(type);
	}

	protected final <TController, TResult> void add(String verb, String path,
		Class<TController> type, BiFunction<Context, TController, TResult> action) {
		TController controller = resolve(type);
		this.route(verb, path, ctx -> action.apply(ctx, controller));
	}

	protected abstract void registerRoutes();
}
