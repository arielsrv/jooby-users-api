package app.extensions;import app.controllers.UserController;import io.jooby.Extension;import io.jooby.Jooby;import org.jetbrains.annotations.NotNull;public class Routes implements Extension {    @Override    public boolean lateinit() {        return Extension.super.lateinit();    }    @Override    public void install(@NotNull Jooby application) {        application.mvc(UserController.class);    }}