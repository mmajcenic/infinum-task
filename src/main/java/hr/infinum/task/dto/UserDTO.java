package hr.infinum.task.dto;

import hr.infinum.task.model.User;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserDTO {

  String email;

  public static UserDTO fromUser(final User user) {
    return UserDTO.builder()
        .email(user.getEmail())
        .build();
  }

}
