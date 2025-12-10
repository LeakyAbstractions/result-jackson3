import com.leakyabstractions.result.api.Result;
import com.leakyabstractions.result.jackson3.ResultModule;

import tools.jackson.databind.JacksonModule;

/**
 * Provides a Jackson 3.x Datatype Module for {@link Result Result} objects.
 * <p>
 * <img src="https://dev.leakyabstractions.com/result-api/result.svg" alt="Result Library">
 * <h2>Jackson 3.x Datatype Module for Result</h2>
 * <p>
 * When using {@link Result Result} objects with <a href="https://github.com/FasterXML/jackson">Jackson 3.x</a> we might
 * run into some problems. The {@link ResultModule Jackson 3.x datatype module for Result} solves them by making Jackson
 * treat results as if they were ordinary objects.
 *
 * @author <a href="https://guillermo.dev/">Guillermo Calvo</a>
 * @see <a href="https://result.leakyabstractions.com/add-ons/jackson3">Quick guide</a>
 * @see <a href="https://leanpub.com/result/">Free book</a>
 * @see <a href="https://github.com/LeakyAbstractions/result-jackson3/">Source code</a>
 * @see ResultModule
 * @see Result Result
 */
module com.leakyabstractions.result.jackson3x {
    exports com.leakyabstractions.result.jackson3;

    requires com.leakyabstractions.result.api;
    requires com.leakyabstractions.result.core;
    requires transitive tools.jackson.databind;

    provides JacksonModule with
            ResultModule;
}
