/* {% if false %} */

package example;

import com.leakyabstractions.result.jackson3.ResultModule;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

@SuppressWarnings({"unused", "java:S125"})
class Fragments {

  static void deserialize() throws Exception {

/* {% elsif include.fragment == "deserialize" %} Deserialize a JSON string */
String json = "{\"version\":\"v2\",\"result\":{\"success\":\"OK\"}}";
ObjectMapper objectMapper = new ObjectMapper(); // Create new object mapper
objectMapper.readValue(json, ApiResponse.class); // Deserialize the response{% endif %}{% if false %}

  }

  static void register() {

/* {% elsif include.fragment == "register_manually" %} Register ResultModule */
JsonMapper.Builder builder = JsonMapper.builder(); // Create new builder
builder.addModule(new ResultModule()); // Register manually
ObjectMapper objectMapper = builder.build(); // Create new object mapper{% endif %}{% if false %}

/* {% elsif include.fragment == "register_automatically" %} Register ResultModule */
builder.findAndAddModules(); // Register automatically{% endif %}{% if false %}

  }
}
// {% endif %}