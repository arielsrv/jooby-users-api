package com.github.controllers;

import com.github.model.UserDto;
import com.github.services.UserService;
import io.jooby.Context;
import io.reactivex.rxjava3.core.Single;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.List;

/** The type User controller. */
@Singleton
public class UserController {

  /** The User service. */
  @Inject public UserService userService;

  /**
   * Gets users.
   *
   * @param ignoredContext the ignored context
   * @return the users
   */
  public Single<List<UserDto>> getUsers(Context ignoredContext) {
    return this.userService.getUsers();
  }
}
