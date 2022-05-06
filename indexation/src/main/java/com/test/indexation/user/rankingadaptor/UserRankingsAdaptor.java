package com.test.indexation.user.rankingadaptor;

import com.test.app.user.indexation.User;
import com.test.indexation.user.UserEvent;
import com.test.ai.enhance.EnhanceUserRequest;
import com.test.ai.enhance.UserEnhanceService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class UserRankingsAdaptor {

  private final UserEnhanceService userEnhanceService;

  public Mono<User> enhanceUser(UserEvent user) {
    return Mono.fromFuture(userEnhanceService.enhance(new EnhanceUserRequest(user.username())))
        .map(
            enhanceUserResponse ->
                new User(enhanceUserResponse.username(), enhanceUserResponse.rank()));
  }
}
