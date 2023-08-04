package app;

import app.controllers.UserController;

public class App extends ApiApplication {

    @Override
    public void RegisterRoutes() {
        Add("GET", "/users", UserController.class,
            (context, userController) -> userController.GetUsers(context));
    }
}
