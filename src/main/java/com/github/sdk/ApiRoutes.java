package com.github.sdk;

import io.jooby.Context;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public abstract class ApiRoutes {

	final List<ApiRoute<?>> routes = new ArrayList<>();

	public ApiRoutes() {
		this.register();
	}

	protected final <TController extends ApiController, TResult> void add(
		String verb,
		String path,
		Class<TController> type,
		BiFunction<ApiContext, TController, TResult> action) {

		ApiRoute<TController> route = new ApiRoute<>();
		route.verb = verb;
		route.path = path;
		route.type = type;
		route.action = action;

		this.routes.add(route);
	}

	protected final <TController extends ApiController, TResult> void get(
		String path,
		Class<TController> type,
		BiFunction<ApiContext, TController, TResult> action) {

		add("GET", path, type, action);
	}

	protected final <TController extends ApiController, TResult> void post(
		String path,
		Class<TController> type,
		BiFunction<ApiContext, TController, TResult> action) {

		add("POST", path, type, action);
	}

	public List<ApiRoute<?>> getRoutes() {
		return this.routes;
	}

	public abstract void register();
}

