/**{% if false %}*/

package example;

import static com.leakyabstractions.result.core.Results.failure;
import static com.leakyabstractions.result.core.Results.success;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.leakyabstractions.result.jackson3.ResultModule;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import tools.jackson.databind.JacksonModule;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.cfg.MapperBuilder;
import tools.jackson.databind.exc.InvalidDefinitionException;
import tools.jackson.databind.json.JsonMapper;

@SuppressWarnings("java:S125")
@DisplayName("Example")
class Example_Test {

/** {% elsif include.test == "serialize_successful_result" %} Test serialization solution with a successful result */
@Test
void serializeSuccessfulResult() {
  // Given
  ApiResponse response = new ApiResponse("v1", success("All good"));
  // When
  ObjectMapper objectMapper = JsonMapper.builder().build();
  String json = objectMapper.writeValueAsString(response);
  // Then
  assertTrue(json.contains("v1"));
  assertTrue(json.contains("All good"));
} // End{% endif %}{% if false %}

/** {% elsif include.test == "serialize_failed_result" %} Test serialization problem with a failed result */
@Test
void serializeFailedResult() {
  // Given
  ApiResponse response = new ApiResponse("v2", failure("Oops"));
  // When
  ObjectMapper objectMapper = JsonMapper.builder().build();
  String json = objectMapper.writeValueAsString(response);
  // Then
  assertTrue(json.contains("v2"));
  assertTrue(json.contains("Oops"));
} // End{% endif %}{% if false %}

    /** {% elsif include.test == "deserialization_problem" %} Test deserialization problem */
@Test
void testDeserializationProblem() {
  // Given
  String json = "{\"version\":\"v3\",\"result\":{\"success\":\"OK\"}}";
  // Then
  ObjectMapper objectMapper = new ObjectMapper();
  InvalidDefinitionException error = assertThrows(InvalidDefinitionException.class,
      () -> objectMapper.readValue(json, ApiResponse.class));
  assertTrue(error.getMessage().startsWith(
      "Cannot construct instance of `com.leakyabstractions.result.api.Result`"));
} // End{% endif %}{% if false %}

@Test
void testExpectedDeserializationErrorMessage() throws Exception {
  // Given
  String json = "{\"version\":\"v3\",\"result\":{\"success\":\"OK\"}}";
  String expected;
  try (BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(
      "deserialization_error.txt")))) {
    expected = br.lines().collect(Collectors.joining());
  }
  // Then
  ObjectMapper objectMapper = new ObjectMapper();
  InvalidDefinitionException error = assertThrows(InvalidDefinitionException.class,
      () -> objectMapper.readValue(json, ApiResponse.class));
  assertTrue(error.getMessage().replaceAll("\\n", "").startsWith(expected));
}

/** {% elsif include.test == "deserialize_successful_result" %} Test deserialization solution with a successful result */
@Test
void deserializeSuccessfulResult() {
  // Given
  String json = "{\"version\":\"v4\",\"result\":{\"success\":\"Yay\"}}";
  // When
  ObjectMapper objectMapper = JsonMapper.builder().findAndAddModules().build();
  ApiResponse response = objectMapper.readValue(json, ApiResponse.class);
  // Then
  assertEquals("v4", response.getVersion());
  assertEquals("Yay", response.getResult().orElse(null));
} // End{% endif %}{% if false %}

/** {% elsif include.test == "deserialize_failed_result" %} Test deserialization solution with a failed result */
@Test
void deserializeFailedResult() {
  // Given
  String json = "{\"version\":\"v5\",\"result\":{\"failure\":\"Nay\"}}";
  // When
  ObjectMapper objectMapper = JsonMapper.builder().findAndAddModules().build();
  ApiResponse response = objectMapper.readValue(json, ApiResponse.class);
  // Then
  assertEquals("v5", response.getVersion());
  assertEquals("Nay", response.getResult().getFailure().orElse(null));
} // End{% endif %}{% if false %}

}
// {% endif %}