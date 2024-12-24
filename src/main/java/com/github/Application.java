package com.github;

import com.github.core.ApiApplication;

public class Application extends ApiApplication {

	@Override
	public void init() {
		this.registerDependencyInjectionModule(ApplicationModule.class);
		this.registerRoutes(Routes.class);
	}
}
