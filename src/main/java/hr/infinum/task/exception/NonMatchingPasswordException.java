package hr.infinum.task.exception;

public class NonMatchingPasswordException extends RuntimeException {

  public NonMatchingPasswordException() {
    super("Invalid password");
  }
}
