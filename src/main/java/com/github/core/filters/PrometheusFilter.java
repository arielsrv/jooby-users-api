package com.github.core.filters;

import io.jooby.Route;
import io.jooby.Route.Filter;
import io.jooby.Route.Handler;
import org.jetbrains.annotations.NotNull;

public class PrometheusFilter implements Route.Filter {

	@NotNull
	@Override
	public Handler apply(@NotNull Handler next) {
		return ctx -> next.apply(ctx);
	}

	@NotNull
	@Override
	public Filter then(@NotNull Filter next) {
		return Filter.super.then(next);
	}

	@NotNull
	@Override
	public Handler then(@NotNull Handler next) {
		return Filter.super.then(next);
	}

	@Override
	public void setRoute(Route route) {
		Filter.super.setRoute(route);
	}
}
