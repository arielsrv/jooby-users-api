package app.services;import app.clients.UserClient;import app.model.PostDto;import app.model.UserDto;import app.model.responses.PostResponse;import io.reactivex.rxjava3.core.Observable;import io.reactivex.rxjava3.core.Scheduler;import io.reactivex.rxjava3.core.Single;import io.reactivex.rxjava3.schedulers.Schedulers;import jakarta.inject.Inject;import jakarta.inject.Singleton;import java.util.ArrayList;import java.util.List;import java.util.concurrent.Executors;@Singletonpublic class UserService {    @Inject    UserClient userClient;    public Single<List<UserDto>> GetUsers() {        return this.userClient.GetUsers()            .flatMapObservable(Observable::fromIterable)            .flatMapSingle(userResponse -> {                UserDto userDto = new UserDto();                userDto.id = userResponse.Id;                userDto.name = userResponse.Name;                return this.userClient.GetPostByUserId(userDto.id)                    .flatMap(postsResponse -> {                        List<PostDto> postDtos = new ArrayList<>();                        for (PostResponse postResponse : postsResponse) {                            PostDto postDto = new PostDto();                            postDto.id = postResponse.id;                            postDto.title = postResponse.title;                            postDtos.add(postDto);                        }                        userDto.posts = postDtos;                        return Single.just(userDto);                    });            }).collect(ArrayList::new, List::add);    }}