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
import io.jooby.jetty.JettyServer;
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
		this.use(rx());
		this.RegisterServer();
	}

	protected void RegisterServer() {
		this.install(Resolve(JettyServer.class));
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
