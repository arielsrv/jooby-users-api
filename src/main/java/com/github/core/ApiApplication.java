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

/**
 * The type Api application.
 */
public abstract class ApiApplication extends Jooby {

	/**
	 * The Injector.
	 */
	protected final Injector injector;

	/**
	 * Instantiates a new Api application.
	 */
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

	/**
	 * Register server.
	 */
	protected void registerServer() {
		this.install(resolve(NettyServer.class));
	}

	/**
	 * Register extensions.
	 */
	protected void registerExtensions() {
		this.install(resolve(GuiceModule.class));
		this.install(resolve(JacksonModule.class));
		this.install(resolve(OpenAPIModule.class));
		this.install(resolve(PrometheusModule.class));
	}

	/**
	 * Resolve t.
	 *
	 * @param <T>  the type parameter
	 * @param type the type
	 * @return the t
	 */
	protected <T> T resolve(Class<T> type) {
		return this.injector.getInstance(type);
	}

	/**
	 * Add.
	 *
	 * @param <TController> the type parameter
	 * @param <TResult>     the type parameter
	 * @param verb          the verb
	 * @param path          the path
	 * @param type          the type
	 * @param action        the action
	 */
	protected final <TController, TResult> void add(String verb, String path,
		Class<TController> type, BiFunction<Context, TController, TResult> action) {
		TController controller = resolve(type);
		this.route(verb, path, ctx -> action.apply(ctx, controller));
	}

	/**
	 * Register routes.
	 */
	protected abstract void registerRoutes();
}
