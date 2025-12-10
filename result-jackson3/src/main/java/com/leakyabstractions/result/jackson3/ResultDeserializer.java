/*
 * Copyright 2025 Guillermo Calvo
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

import java.util.List;
import java.util.Optional;

import com.leakyabstractions.result.api.Result;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationConfig;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.deser.std.StdDeserializer;

/**
 * Deserializes {@link Result} objects.
 *
 * @author <a href="https://guillermo.dev/">Guillermo Calvo</a>
 */
class ResultDeserializer extends StdDeserializer<Result<?, ?>> {

    private final JavaType builderType;

    ResultDeserializer(DeserializationConfig config, JavaType valueType) {
        super(valueType);
        this.builderType = getBuilderType(config, valueType.getBindings().getTypeParameters());
    }

    @Override
    public Result<?, ?> deserialize(JsonParser parser, DeserializationContext context)
            throws JacksonException {
        return Optional.ofNullable(this.readBuilder(parser, context))
                .map(ResultBuilder::build)
                .orElse(null);
    }

    private ResultBuilder<?, ?> readBuilder(JsonParser parser, DeserializationContext context) {
        if (this.builderType == null) return parser.readValueAs(ResultBuilder.class);
        return (ResultBuilder<?, ?>) context.findRootValueDeserializer(this.builderType).deserialize(parser, context);
    }

    private static JavaType getBuilderType(DeserializationConfig config, List<JavaType> typeParams) {
        if (typeParams.size() != 2) return null;
        return config
                .getTypeFactory()
                .constructSimpleType(ResultBuilder.class, typeParams.toArray(new JavaType[2]));
    }
}
