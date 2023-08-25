package com.github.core.modules;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.core.providers.GuiceModuleProvider;
import com.github.core.providers.JacksonModuleProvider;
import com.github.core.providers.ObjectMapperProvider;
import com.google.inject.AbstractModule;
import io.jooby.OpenAPIModule;
import io.jooby.guice.GuiceModule;
import io.jooby.jackson.JacksonModule;
import io.jooby.netty.NettyServer;

public class AppModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(NettyServer.class).toInstance(new NettyServer());
		bind(PrometheusModule.class).toInstance(new PrometheusModule());
		bind(OpenAPIModule.class).toInstance(new OpenAPIModule());
		bind(ObjectMapper.class).toProvider(ObjectMapperProvider.class);
		bind(JacksonModule.class).toProvider(JacksonModuleProvider.class);
		bind(GuiceModule.class).toProvider(GuiceModuleProvider.class);
	}
}
