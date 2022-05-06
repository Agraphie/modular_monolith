package com.test.app.user.indexation;


import lombok.Builder;

@Builder
public record User(String username, String rank) {}
