package com.test.indexation.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.indexation.user.rankingadaptor.UserRankingsAdaptor;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.redis.connection.ReactiveSubscription;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.logging.Level;

@Service
@AllArgsConstructor
@Log
public class UserEventConsumer {
  private final UserRepository userRepository;
  private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;
  private final ObjectMapper objectMapper;
  private final UserRankingsAdaptor userRankingsAdaptor;

  @PostConstruct
  void subscribe() {
    reactiveRedisTemplate
        .listenToChannel("users")
        .doOnSubscribe(subscription -> log.info("Subscribed to user channel"))
        .map(ReactiveSubscription.Message::getMessage)
        .flatMap(
            m -> {
              try {
                return Mono.just(objectMapper.readValue(m, UserEvent.class));
              } catch (JsonProcessingException e) {
                return Mono.error(e);
              }
            })
        .flatMap(userRankingsAdaptor::enhanceUser)
        .flatMap(userRepository::storeUser)
        .subscribe(user -> {}, throwable -> log.log(Level.WARNING, "", throwable));
  }
}
