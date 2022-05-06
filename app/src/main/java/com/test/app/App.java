package com.test.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.test.ai", "com.test.app"})
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
