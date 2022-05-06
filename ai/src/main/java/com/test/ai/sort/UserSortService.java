package com.test.ai.sort;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UserSortService {

  public List<String> sortUsers(List<String> users) {
    if (Math.random() > 0.5) {
      throw new RuntimeException("Chaos monkey");
    }

    ArrayList<String> sortedUsers = new ArrayList<>(users);
    Collections.shuffle(sortedUsers);

    return sortedUsers;
  }
}
