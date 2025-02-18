package app;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.Application;
import io.jooby.StatusCode;
import io.jooby.test.JoobyTest;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.Test;

@JoobyTest(Application.class)
public class IntegrationTest {

	static final OkHttpClient client = new OkHttpClient();

	@Test
	public void get_Users(int serverPort) throws IOException {
		Request req = new Request.Builder()
			.url("http://localhost:" + serverPort + "/users")
			.build();

		try (Response rsp = client.newCall(req).execute()) {
			assertEquals(StatusCode.OK.value(), rsp.code());
		}
	}
}
