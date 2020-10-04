package com.infinum.task.user.model;

import lombok.Builder;
import lombok.Value;
import java.util.List;

@Value
@Builder
public class UserDTO {

    String email;

    List<String> cities;

}
