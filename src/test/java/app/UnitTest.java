package app;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.jooby.StatusCode;
import io.jooby.test.MockRouter;
import org.junit.jupiter.api.Test;

public class UnitTest {

    @Test
    public void welcome() {
        MockRouter router = new MockRouter(new App());
        router.get("/", rsp -> {
            assertEquals("Welcome to Jooby!", rsp.value());
            assertEquals(StatusCode.OK, rsp.getStatusCode());
        });
    }
}
