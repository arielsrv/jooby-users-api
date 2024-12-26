package com.github;

import com.github.sdk.http.RestClient;
import com.github.sdk.http.RestClientBuilder;
import com.github.sdk.modules.InjectionModule;
import com.google.inject.name.Names;


public class ApplicationModule extends InjectionModule {

	@Override
	protected void configure() {
		super.configure();

		RestClient restClient = RestClientBuilder.newBuilder()
			.setBaseUrl("https://gorest.co.in/public/v2")
			.setConnectionTimeout(2000)
			.setCallTimeout(2000)
			.build();

		bind(RestClient.class).annotatedWith(Names.named("gorest-client")).toInstance(restClient);
	}
}

