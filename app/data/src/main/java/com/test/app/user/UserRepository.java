package com.test.app.user;

import com.test.app.user.indexation.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {

  List<String> getUsers() {
    return loadFromDataSource().stream().map(User::username).toList();
  }

  private List<User> loadFromDataSource() {
    return List.of(new User("User1", "1"), new User("User2", "2"), new User("User3", "3"));
  }
}
