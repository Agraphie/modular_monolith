package com.test.indexation.user;

import com.test.app.user.indexation.User;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Log
class UserRepository {
  Mono<User> storeUser(User user) {
    return Mono.just(user);
  }
}
