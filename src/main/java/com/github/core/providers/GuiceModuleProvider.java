package com.github.core.providers;

import com.google.inject.Injector;
import io.jooby.guice.GuiceModule;
import jakarta.inject.Inject;
import jakarta.inject.Provider;

/** The type Guice module provider. */
public class GuiceModuleProvider implements Provider<GuiceModule> {

    /** The Injector. */
    @Inject
    Injector injector;

    @Override
    public GuiceModule get() {
        return new GuiceModule(this.injector);
    }
}
