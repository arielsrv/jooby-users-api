package com.github.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.github.clients.PostClient;
import com.github.clients.UserClient;
import com.github.model.PostDto;
import com.github.model.UserDto;
import com.github.model.responses.PostResponse;
import com.github.model.responses.UserResponse;
import com.google.common.cache.CacheBuilder;
import io.reactivex.rxjava3.core.Single;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserServiceUnitTest {

    private UserClient userClient;
    private PostClient postClient;

    @BeforeEach
    public void setUp() {
        this.userClient = mock(UserClient.class);
        this.postClient = mock(PostClient.class);
    }

    @Test
    public void get_Users() {
        when(this.userClient.getUsers()).thenReturn(getUsers());
        when(this.postClient.getPostByUserId(anyLong())).thenReturn(getPosts());

        UserService userService = new UserService();
        userService.userClient = this.userClient;
        userService.postClient = this.postClient;

        List<UserDto> actual = userService.getUsers().blockingGet();
        assertNotNull(actual);
        assertEquals(1, actual.size());
    }

    @Test
    public void get_Users_From_Cache() {
        when(this.userClient.getUsers()).thenReturn(getUsers());

        UserService userService = new UserService();
        userService.appCache = CacheBuilder.newBuilder().build();
        userService.appCache.put(1L, getPostsDto());
        userService.userClient = this.userClient;

        List<UserDto> actual = userService.getUsers().blockingGet();
        assertNotNull(actual);
        assertEquals(1, actual.size());
    }

    private Single<List<UserResponse>> getUsers() {
        UserResponse userResponse = new UserResponse();
        userResponse.id = 1L;
        userResponse.name = "John Doe";

        return Single.just(List.of(userResponse));
    }

    private Single<List<PostResponse>> getPosts() {
        PostResponse postResponse = new PostResponse();
        postResponse.id = 1;
        postResponse.title = "title";

        return Single.just(List.of(postResponse));
    }

    private List<PostDto> getPostsDto() {
        PostDto postDto = new PostDto();
        postDto.id = 1;
        postDto.title = "title";

        List<PostDto> postDtos = new ArrayList<>();
        postDtos.add(postDto);

        return postDtos;
    }
}
