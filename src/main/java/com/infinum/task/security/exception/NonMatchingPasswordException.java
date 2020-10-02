package com.infinum.task.security.exception;

public class NonMatchingPasswordException extends RuntimeException {

  public NonMatchingPasswordException() {
    super("Invalid password");
  }
}
