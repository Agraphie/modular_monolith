package com.test.app.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final UserRankingsAdaptor userRankingsAdaptor;

  List<String> listUsers() {
    List<String> users = userRepository.getUsers();
    return userRankingsAdaptor.sortUsers(users);
  }
}
