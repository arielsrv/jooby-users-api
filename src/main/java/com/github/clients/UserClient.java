package com.github.clients;

import com.github.core.http.Response;
import com.github.core.http.RestClient;
import com.github.model.responses.UserResponse;
import io.reactivex.rxjava3.core.Flowable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;


@Singleton
public class UserClient {

    private final String baseUrl;
    @Inject
    RestClient restClient;

    public UserClient() {
        this.baseUrl = "https://gorest.co.in/public/v2";
    }

    public Flowable<UserResponse> GetUsers() {
        String apiUrl = "%s/users".formatted(this.baseUrl);

        return this.restClient.GetFlowable(apiUrl, UserResponse.class)
            .map(Response::GetData);
    }
}
