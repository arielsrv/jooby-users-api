package com.github.core.modules;

import com.github.core.filters.PrometheusFilter;
import io.jooby.Extension;
import io.jooby.Jooby;
import org.jetbrains.annotations.NotNull;

public class PrometheusModule implements Extension {

	private final String path;

	public PrometheusModule() {
		this.path = "/metrics";
	}

	@Override
	public void install(@NotNull Jooby application) {
		application.use(new PrometheusFilter());
		application.get(this.path, ctx -> "[metrics]");
	}
}
