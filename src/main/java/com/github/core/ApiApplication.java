package com.github.core;

import static com.google.inject.Guice.createInjector;
import static io.jooby.rxjava3.Reactivex.rx;

import com.github.ApplicationModule;
import com.github.Routes;
import com.github.core.modules.AppModule;
import com.github.core.modules.PrometheusModule;
import com.google.common.base.Strings;
import com.google.inject.Injector;
import io.jooby.Context;
import io.jooby.EnvironmentOptions;
import io.jooby.Jooby;
import io.jooby.OpenAPIModule;
import io.jooby.guice.GuiceModule;
import io.jooby.jackson.JacksonModule;
import io.jooby.netty.NettyServer;
import java.util.List;
import java.util.function.BiFunction;

public abstract class ApiApplication extends Jooby {

	protected final Injector injector;
	private List<ApiRoute<?, ?>> routes;

	protected ApiApplication() {
		this.injector = createInjector(new AppModule());
		this.init();
		this.use(rx());
		this.registerServer();
		this.registerExtensions();

		this.routes.forEach(route -> {
			Object controller = resolve(route.type);
			this.route(route.verb, route.path, ctx -> {
				@SuppressWarnings("unchecked")
				BiFunction<Context, Object, ?> action = (BiFunction<Context, Object, ?>) route.action;
				return action.apply(ctx, controller);
			});
		});

		String envVar = System.getenv("ENV");
		if (Strings.isNullOrEmpty(envVar)) {
			envVar = "dev";
		}

		this.setEnvironmentOptions(
			new EnvironmentOptions().setFilename("config/config.%s.conf".formatted(envVar)));
	}

	public abstract void init();


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

	protected void registerRoutes(Class<Routes> routesClass) {
		try {
			this.routes = routesClass.getDeclaredConstructor().newInstance().getRoutes();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected void registerDependencyInjectionModule(
		Class<ApplicationModule> applicationModuleClass) {
	}
}
