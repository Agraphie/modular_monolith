package com.test.ai.enhance;

import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class UserEnhanceService {

  public CompletableFuture<EnhanceUserResponse> enhance(EnhanceUserRequest userRequest) {
    String username = userRequest.username();
    EnhanceUserResponse userResponse =
        new EnhanceUserResponse(username, String.valueOf(Math.random()));
    return CompletableFuture.completedFuture(userResponse);
  }
}
