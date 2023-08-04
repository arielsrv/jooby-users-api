package app.services;

import app.clients.PostClient;
import app.clients.UserClient;
import app.model.PostDto;
import app.model.UserDto;
import app.model.responses.PostResponse;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class UserService {

    @Inject
    public UserClient userClient;

    @Inject
    public PostClient postClient;

    public Single<List<UserDto>> GetUsers() {
        return this.userClient.GetUsers().flatMapObservable(Observable::fromIterable)
            .flatMapSingle(userResponse -> {

                UserDto userDto = new UserDto();
                userDto.id = userResponse.Id;
                userDto.name = userResponse.Name;

                return this.postClient.GetPostByUserId(userDto.id).flatMap(postsResponse -> {
                    List<PostDto> postDtos = new ArrayList<>();
                    for (PostResponse postResponse : postsResponse) {
                        PostDto postDto = new PostDto();
                        postDto.id = postResponse.id;
                        postDto.title = postResponse.title;

                        postDtos.add(postDto);
                    }
                    userDto.postDtos = postDtos;

                    return Single.just(userDto);
                });
            }).collect(ArrayList::new, List::add);
    }
}
