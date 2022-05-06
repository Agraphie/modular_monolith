package com.test.app.user;

import com.test.ai.sort.UserSortService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Level;

@Service
@Log
@AllArgsConstructor
public class UserRankingsAdaptor {

  private final UserSortService userSortService;

  @CircuitBreaker(name = "cb-user-sort-call", fallbackMethod = "sortUsersFallback")
  List<String> sortUsers(List<String> users) {
    return userSortService.sortUsers(users);
  }

  public List<String> sortUsersFallback(List<String> users, Exception exception) {
    log.log(Level.INFO, "Returning fallback");
    return users;
  }
}
