package hr.infinum.task.exception;

public class InvalidPasswordException extends RuntimeException {

  public InvalidPasswordException() {
    super("Invalid password");
  }
}
