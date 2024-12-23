package com.github;

import com.github.core.ApiApplication;

/**
 * The type App.
 */
public class Application extends ApiApplication {

	@Override
	public void init() {
		this.registerDependencyInjectionModule(ApplicationModule.class);
		this.registerRoutes(Routes.class);
	}
}
