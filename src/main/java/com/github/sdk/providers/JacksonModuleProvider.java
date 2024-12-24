package com.github.sdk.providers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jooby.jackson.JacksonModule;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

@Singleton
public class JacksonModuleProvider implements Provider<JacksonModule> {

	@Inject
	ObjectMapper objectMapper;

	@Override
	public JacksonModule get() {
		return new JacksonModule(this.objectMapper);
	}
}
