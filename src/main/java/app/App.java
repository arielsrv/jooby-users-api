package app;

import app.controllers.UserController;
import app.core.ApiApplication;

public class App extends ApiApplication {

    public static void main(final String[] args) {
        ApiApplication app = new App();
        app.start();
    }

    @Override
    public void RegisterRoutes() {
        Add(GET, "/users", UserController.class,
            (context, userController) -> userController.GetUsers(context));
    }
}
