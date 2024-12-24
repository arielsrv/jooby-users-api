package com.github.core;import io.jooby.Context;import java.util.ArrayList;import java.util.List;import java.util.function.BiFunction;public abstract class ApiRoutes {	final List<ApiRoute<?, ?>> routes = new ArrayList<>();	public ApiRoutes() {		this.register();	}	protected final <TController, TResult> void add(		String verb,		String path,		Class<TController> type,		BiFunction<Context, TController, TResult> action) {		ApiRoute<TController, TResult> route = new ApiRoute<>(verb, path, type, action);		this.routes.add(route);	}	public List<ApiRoute<?, ?>> getRoutes() {		return this.routes;	}	public abstract void register();}