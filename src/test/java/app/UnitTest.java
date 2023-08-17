package app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.github.controllers.UserController;
import com.github.model.UserDto;
import com.github.services.UserService;
import io.jooby.Context;
import io.reactivex.rxjava3.core.Single;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;


public class UnitTest {

	@Test
	public void welcome() {
		Context context = mock(Context.class);

		UserService userService = mock(UserService.class);
		when(userService.GetUsers()).thenReturn(GetUsers());

		UserController userController = new UserController();
		userController.userService = userService;

		List<UserDto> actual = userController.GetUsers(context).blockingGet();
		assertNotNull(actual);
		assertEquals(1, actual.size());
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
