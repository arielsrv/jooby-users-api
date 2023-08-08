package com.github.core.providers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jooby.jackson.JacksonModule;
import jakarta.inject.Inject;
import jakarta.inject.Provider;

public class JacksonModuleProvider implements Provider<JacksonModule> {

    @Inject
    ObjectMapper objectMapper;

    @Override
    public JacksonModule get() {
        return new JacksonModule(this.objectMapper);
    }
}