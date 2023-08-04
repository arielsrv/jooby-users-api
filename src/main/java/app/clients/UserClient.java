package app.clients;

import app.core.http.RestClient;
import app.model.responses.UserResponse;
import io.reactivex.rxjava3.core.Single;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.Arrays;
import java.util.List;


@Singleton
public class UserClient {

    private final String baseUrl;
    @Inject
    RestClient restClient;

    public UserClient() {
        this.baseUrl = "https://gorest.co.in/public/v2";
    }

    public Single<List<UserResponse>> GetUsers() {
        String apiUrl = "%s/users".formatted(this.baseUrl);
        return this.restClient.Get(apiUrl, UserResponse[].class)
            .map(response -> Arrays.stream(response).toList());
    }
}
