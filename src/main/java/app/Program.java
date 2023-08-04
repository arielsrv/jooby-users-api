package app;

import io.jooby.Jooby;

public class Program extends Jooby {

    public static void main(final String[] args) {
        ApiApplication app = new App();
        app.start();
    }
}

