package com.github.sdk;

import static com.google.inject.Guice.createInjector;
import static io.jooby.rxjava3.Reactivex.rx;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.Routes;
import com.github.sdk.modules.PrometheusModule;
import com.google.common.base.Strings;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import io.jooby.EnvironmentOptions;
import io.jooby.Jooby;
import io.jooby.OpenAPIModule;
import io.jooby.guice.GuiceModule;
import io.jooby.jackson.JacksonModule;
import io.jooby.netty.NettyServer;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.function.BiFunction;

public abstract class ApiApplication extends Jooby {

	protected final Injector injector;
	private List<ApiRoute<?>> routes;
	private AbstractModule module;


	protected ApiApplication() {
		this.init();
		this.injector = createInjector(this.module);
		this.use(rx());
		this.registerServer();
		this.registerExtensions();

		this.routes.forEach(route -> {
			ApiController controller = Resolve(route.type);
			this.route(route.verb, route.path, ctx -> {
				@SuppressWarnings("unchecked")
				BiFunction<ApiContext, ApiController, ?> action = (BiFunction<ApiContext, ApiController, ?>) route.action;
				ObjectMapper objectMapper = this.Resolve(ObjectMapper.class);
				ApiContext apiCtx = new ApiContext(ctx, objectMapper);
				return action.apply(apiCtx, controller);
			});
		});

		String envVar = System.getenv("ENV");
		if (Strings.isNullOrEmpty(envVar)) {
			envVar = "dev";
		}

		this.setEnvironmentOptions(
			new EnvironmentOptions().setFilename("config/config.%s.conf".formatted(envVar)));

		String message = this.getConfig().getString("message");
		System.out.println("Message: " + message);
	}

	public abstract void init();

	protected void registerServer() {
		this.install(Resolve(NettyServer.class));
	}

	protected void registerExtensions() {
		this.install(Resolve(GuiceModule.class));
		this.install(Resolve(JacksonModule.class));
		this.install(Resolve(OpenAPIModule.class));
		this.install(Resolve(PrometheusModule.class));
	}

	protected <T> T Resolve(Class<T> type) {
		return this.injector.getInstance(type);
	}

	protected void registerRoutes(Class<Routes> routesClass) {
		try {
			this.routes = routesClass.getDeclaredConstructor().newInstance().getRoutes();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected void registerInjectionModule(
		Class<? extends AbstractModule> applicationModuleClass) {
		try {
			this.module = applicationModuleClass.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException |
				 NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}
}
