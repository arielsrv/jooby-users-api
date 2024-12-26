package com.github.sdk;

import io.jooby.Context;
import java.util.function.BiFunction;

public class ApiRoute<TController extends ApiController> {

	public String verb;
	public String path;
	public Class<TController> type;
	public BiFunction<Context, TController, ?> action;
}
