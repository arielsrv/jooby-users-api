package com.github;import com.github.sdk.http.RestClient;import com.github.sdk.modules.InjectionModule;import com.google.inject.name.Names;public class ApplicationModule extends InjectionModule {	@Override	protected void configure() {		super.configure();		bind(RestClient.class).annotatedWith(Names.named("gorest-client"))			.toInstance(new RestClient("https://gorest.co.in/public/v2"));	}}