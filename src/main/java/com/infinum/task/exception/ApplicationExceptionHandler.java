package com.infinum.task.exception;

import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import com.infinum.task.security.exception.NonMatchingPasswordException;
import com.infinum.task.security.exception.UserNotFoundException;
import com.infinum.task.user.exception.CityAlreadyFavouredException;
import com.infinum.task.user.exception.CityNotFavouredException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler({
      CityAlreadyFavouredException.class,
      CityNotFavouredException.class,
      IllegalArgumentException.class,
      IllegalStateException.class
  })
  public ResponseEntity<String> handleCommonExceptions(final RuntimeException e) {
    return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({
      EntityNotFoundException.class
  })
  public ResponseEntity<String> handleEntityNotFound(final RuntimeException e) {
    return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler({
      NonMatchingPasswordException.class,
      UserNotFoundException.class
  })
  public ResponseEntity<String> handleAuthenticationExceptions(final RuntimeException e) {
    return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<String> handleDataIntegrityViolationException(final DataIntegrityViolationException e) {
    final var cause = e.getCause();
    if (cause instanceof ConstraintViolationException) {
      return new ResponseEntity<>(((ConstraintViolationException) cause).getSQLException().getMessage(),
          HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
  }

  @NonNull
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(@NonNull final MethodArgumentNotValidException ex,
      @NonNull final HttpHeaders headers,
      @NonNull final HttpStatus status,
      @NonNull final WebRequest request) {
    return new ResponseEntity<>(ex.getBindingResult().getAllErrors()
        .stream()
        .map(ObjectError::getDefaultMessage)
        .collect(Collectors.joining("\n")),
        HttpStatus.BAD_REQUEST);
  }

  @NonNull
  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(@NonNull final HttpMessageNotReadableException ex,
      @NonNull final HttpHeaders headers,
      @NonNull final HttpStatus status,
      @NonNull final WebRequest request) {
    return new ResponseEntity<>("Body is not readable", HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(javax.validation.ConstraintViolationException.class)
  public ResponseEntity<String> handleConstraintViolationException(javax.validation.ConstraintViolationException e) {
    return new ResponseEntity<>(e.getConstraintViolations()
        .stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.joining("\n")),
        HttpStatus.BAD_REQUEST);
  }

}
