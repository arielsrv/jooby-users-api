package com.github.core.providers;

import com.google.inject.Injector;
import io.jooby.guice.GuiceModule;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

@Singleton
public class GuiceModuleProvider implements Provider<GuiceModule> {

	@Inject
	Injector injector;

	@Override
	public GuiceModule get() {
		return new GuiceModule(this.injector);
	}
}
