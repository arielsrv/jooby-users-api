package com.github;

import static io.jooby.Jooby.runApp;

import io.jooby.ExecutionMode;

public class Program {

	public static void main(final String[] args) {
		runApp(args, ExecutionMode.EVENT_LOOP, Application::new);
	}
}
