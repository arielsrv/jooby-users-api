package app.modules;

import app.providers.GuiceModuleProvider;
import app.providers.JacksonModuleProvider;
import app.providers.ObjectMapperProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import io.jooby.OpenAPIModule;
import io.jooby.guice.GuiceModule;
import io.jooby.jackson.JacksonModule;
import io.jooby.netty.NettyServer;

public class AppModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(NettyServer.class).toInstance(new NettyServer());
        bind(OpenAPIModule.class).toInstance(new OpenAPIModule());
        bind(ObjectMapper.class).toProvider(ObjectMapperProvider.class);
        bind(JacksonModule.class).toProvider(JacksonModuleProvider.class);
        bind(GuiceModule.class).toProvider(GuiceModuleProvider.class);
    }
}
