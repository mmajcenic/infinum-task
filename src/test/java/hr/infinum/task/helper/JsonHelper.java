package hr.infinum.task.helper;

import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

public final class JsonHelper {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  private JsonHelper() {
  }

  public static String toJson(final Object object) throws JsonProcessingException {
    return OBJECT_MAPPER.writeValueAsString(object);
  }



}
