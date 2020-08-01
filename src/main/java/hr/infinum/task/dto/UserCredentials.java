package hr.infinum.task.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Value;
import org.hibernate.validator.constraints.Length;

@Value
@Builder
public class UserCredentials {

  @Email(message = "Invalid email")
  @NotEmpty(message = "Email is required")
  String email;

  @Length(min = 8)
  @NotEmpty(message = "Password is required")
  String password;

}
