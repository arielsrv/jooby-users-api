package com.github.services;import static org.junit.jupiter.api.Assertions.assertEquals;import static org.junit.jupiter.api.Assertions.assertNotNull;import static org.mockito.ArgumentMatchers.anyLong;import static org.mockito.Mockito.mock;import static org.mockito.Mockito.when;import com.github.clients.PostClient;import com.github.clients.UserClient;import com.github.model.UserDto;import com.github.model.responses.PostResponse;import com.github.model.responses.UserResponse;import io.reactivex.rxjava3.core.Flowable;import io.reactivex.rxjava3.core.Single;import java.util.List;import org.junit.jupiter.api.BeforeEach;import org.junit.jupiter.api.Test;public class UserServiceUnitTest {    private UserClient userClient;    private PostClient postClient;    @BeforeEach    public void setUp() {        this.userClient = mock(UserClient.class);        this.postClient = mock(PostClient.class);    }    @Test    public void GetUsers_Ok() {        when(this.userClient.GetUsers()).thenReturn(GetUsers());        when(this.postClient.GetPostByUserId(anyLong())).thenReturn(GetPosts());        UserService userService = new UserService();        userService.userClient = this.userClient;        userService.postClient = this.postClient;        List<UserDto> actual = userService.GetUsers().toList().blockingGet();        assertNotNull(actual);        assertEquals(1, actual.size());    }    private Flowable<UserResponse> GetUsers() {        UserResponse userResponse = new UserResponse();        userResponse.Id = 1L;        userResponse.Name = "John Doe";        return Flowable.just(userResponse);    }    private Single<List<PostResponse>> GetPosts() {        PostResponse postResponse = new PostResponse();        postResponse.id = 1;        postResponse.title = "title";        return Single.just(List.of(postResponse));    }}