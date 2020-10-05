package com.infinum.task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class InfinumTaskApplication {

  public static void main(String[] args) {
    SpringApplication.run(InfinumTaskApplication.class, args);
  }

}
