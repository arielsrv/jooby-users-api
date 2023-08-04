package app;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import app.model.UserDto;
import app.services.UserService;
import io.jooby.Context;
import io.jooby.test.MockRouter;
import io.reactivex.rxjava3.core.Single;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;


public class UnitTest {

    @Test
    public void welcome() {
        UserService userService = mock(UserService.class);
        when(userService.GetUsers()).thenReturn(GetUsers());

        Context context = mock(Context.class);
        when(context.require(UserService.class)).thenReturn(userService);

        MockRouter router = new MockRouter(new App());
        router.get("/users", context).value();
    }

    private Single<List<UserDto>> GetUsers() {
        UserDto userDto = new UserDto();
        userDto.id = 1;
        userDto.name = "John Doe";

        List<UserDto> users = new ArrayList<>();
        users.add(userDto);

        return Single.just(users);
    }
}
