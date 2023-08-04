package app.providers;

import com.google.inject.Injector;
import io.jooby.guice.GuiceModule;
import jakarta.inject.Inject;
import jakarta.inject.Provider;

public class GuiceModuleProvider implements Provider<GuiceModule> {

    @Inject
    Injector injector;

    @Override
    public GuiceModule get() {
        return new GuiceModule(this.injector);
    }
}
