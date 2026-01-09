/*
 * Copyright 2026 Guillermo Calvo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.leakyabstractions.result.jackson3;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.LIST;
import static org.assertj.core.api.InstanceOfAssertFactories.OPTIONAL;
import static org.assertj.core.api.InstanceOfAssertFactories.optional;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.leakyabstractions.result.api.Result;
import com.leakyabstractions.result.core.Results;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

/**
 * Tests for {@link ResultModule}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("ResultModule")
class ResultModule_Test {

    static class Foobar<T> {

        @JsonProperty
        T foo;

        @JsonProperty
        Boolean bar;

        Foobar() {}

        Foobar(T foo, boolean bar) {
            this.foo = foo;
            this.bar = bar;
        }
    }

    @Test
    void read_successful_result() {
        // Given
        final ObjectMapper mapper = JsonMapper.builder().findAndAddModules().build();
        final String json = "{\"success\":\"SUCCESS\"}";
        final TypeReference<Result<String, Integer>> RESULT_TYPE = new TypeReference<Result<String, Integer>>() {
        };
        // When
        final Result<String, Integer> result = mapper.readValue(json, RESULT_TYPE);
        // Then
        assertThat(result).extracting("success", OPTIONAL).contains("SUCCESS");
    }

    @Test
    void read_successful_map_result() {
        // Given
        final ObjectMapper mapper = JsonMapper.builder().findAndAddModules().build();
        final String json = "{\"success\":{\"x\":\"SUCCESS\",\"y\":123}}";
        // When
        final Result<?, ?> result = mapper.readValue(json, Result.class);
        // Then
        assertThat(result)
                .extracting("success", OPTIONAL)
                .hasValueSatisfying(
                        x -> {
                            assertThat(x).hasFieldOrPropertyWithValue("x", "SUCCESS");
                            assertThat(x).hasFieldOrPropertyWithValue("y", 123);
                        });
    }

    @Test
    void read_failed_result() {
        // Given
        final ObjectMapper mapper = JsonMapper.builder().findAndAddModules().build();
        final String json = "{\"failure\":404}";
        final TypeReference<Result<String, Integer>> type = new TypeReference<Result<String, Integer>>() {
        };
        // When
        final Result<String, Integer> result = mapper.readValue(json, type);
        // Then
        assertThat(result).extracting("failure", OPTIONAL).contains(404);
    }

    @Test
    void read_null() {
        // Given
        final ObjectMapper mapper = JsonMapper.builder().findAndAddModules().build();
        final String json = "null";
        // When
        final Result<?, ?> result = mapper.readValue(json, Result.class);
        // Then
        assertThat(result).isNull();
    }

    @Test
    void read_empty_result_as_null() {
        // Given
        final ObjectMapper mapper = JsonMapper.builder().findAndAddModules().build();
        final String json = "{}";
        final TypeReference<Result<String, Integer>> type = new TypeReference<Result<String, Integer>>() {
        };
        // When
        final Result<String, Integer> result = mapper.readValue(json, type);
        // Then
        assertThat(result).isNull();
    }

    @Test
    void read_malformed_result_as_failure() {
        // Given
        final ObjectMapper mapper = JsonMapper.builder().findAndAddModules().build();
        final String json = "{\"success\":123,\"failure\":321}";
        final TypeReference<Result<String, Integer>> type = new TypeReference<Result<String, Integer>>() {
        };
        // When
        final Result<String, Integer> result = mapper.readValue(json, type);
        // Then
        assertThat(result).extracting("failure", OPTIONAL).contains(321);
    }

    @Test
    void write_successful_result() {
        // Given
        final ObjectMapper mapper = JsonMapper.builder()
                .findAndAddModules()
                .changeDefaultPropertyInclusion(x -> x.withValueInclusion(NON_NULL))
                .build();
        final Result<?, ?> result = Results.success("OK");
        // When
        final String json = mapper.writeValueAsString(result);
        // Then
        assertThat(json).isEqualTo("{\"success\":\"OK\"}");
    }

    @Test
    void write_failed_result() {
        // Given
        final ObjectMapper mapper = JsonMapper.builder()
                .findAndAddModules()
                .changeDefaultPropertyInclusion(x -> x.withValueInclusion(NON_NULL))
                .build();
        final Result<?, ?> result = Results.failure("FAILURE");
        // When
        final String json = mapper.writeValueAsString(result);
        // Then
        assertThat(json).isEqualTo("{\"failure\":\"FAILURE\"}");
    }

    @Test
    void read_complex_successful_result() {
        // Given
        final ObjectMapper mapper = JsonMapper.builder().findAndAddModules().build();
        final String json = "{\"success\":{\"foo\":[1,2,3],\"bar\":true}}";
        final TypeReference<Result<Foobar<List<Integer>>, ?>> type = new TypeReference<Result<Foobar<List<Integer>>, ?>>() {
        };
        // When
        final Result<Foobar<List<Integer>>, ?> result = mapper.readValue(json, type);
        // Then
        assertThat(result)
                .extracting("success", optional(Foobar.class))
                .hasValueSatisfying(
                        x -> {
                            assertThat(x.foo).asInstanceOf(LIST).containsExactly(1, 2, 3);
                            assertThat(x.bar).isTrue();
                        });
    }

    @Test
    void read_complex_failed_result() {
        // Given
        final ObjectMapper mapper = JsonMapper.builder().findAndAddModules().build();
        final String json = "{\"failure\":{\"foo\":404}}";
        final TypeReference<Result<Integer, Foobar<Long>>> type = new TypeReference<Result<Integer, Foobar<Long>>>() {
        };
        // When
        final Result<Integer, Foobar<Long>> result = mapper.readValue(json, type);
        // Then
        assertThat(result)
                .extracting("failure", optional(Foobar.class))
                .hasValueSatisfying(
                        x -> {
                            assertThat(x.foo).isEqualTo(404L);
                            assertThat(x.bar).isNull();
                        });
    }

    @Test
    void read_complex_null() {
        // Given
        final ObjectMapper mapper = JsonMapper.builder().findAndAddModules().build();
        final String json = "null";
        final TypeReference<Result<Foobar<Float>, String>> type = new TypeReference<Result<Foobar<Float>, String>>() {
        };
        // When
        final Result<?, ?> result = mapper.readValue(json, type);
        // Then
        assertThat(result).isNull();
    }

    @Test
    void read_complex_empty_result_as_null() {
        // Given
        final ObjectMapper mapper = JsonMapper.builder().findAndAddModules().build();
        final String json = "{}";
        final TypeReference<Result<Foobar<String>, BigDecimal>> type = new TypeReference<Result<Foobar<String>, BigDecimal>>() {
        };
        // When
        final Result<Foobar<String>, BigDecimal> result = mapper.readValue(json, type);
        // Then
        assertThat(result).isNull();
    }

    @Test
    void read_malformed_complex_result_as_failure() {
        // Given
        final ObjectMapper mapper = JsonMapper.builder().findAndAddModules().build();
        final String json = "{\"success\":{\"foo\":\"12.34\",\"bar\":true},\"failure\":321}";
        final TypeReference<Result<Foobar<BigDecimal>, Long>> type = new TypeReference<Result<Foobar<BigDecimal>, Long>>() {
        };
        // When
        final Result<Foobar<BigDecimal>, Long> result = mapper.readValue(json, type);
        // Then
        assertThat(result).extracting("failure", OPTIONAL).contains(321L);
    }
}
