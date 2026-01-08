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

import com.leakyabstractions.result.api.Result;

import tools.jackson.databind.BeanDescription;
import tools.jackson.databind.DeserializationConfig;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.ValueDeserializer;
import tools.jackson.databind.deser.Deserializers;

/**
 * Finds deserializers for {@link Result} objects.
 *
 * @author <a href="https://guillermo.dev/">Guillermo Calvo</a>
 */
class ResultDeserializers extends Deserializers.Base {

    @Override
    public ValueDeserializer<?> findBeanDeserializer(
            JavaType type, DeserializationConfig config, BeanDescription.Supplier beanDescRef) {
        if (hasDeserializerFor(config, type.getRawClass())) {
            return new ResultDeserializer(config, type);
        }
        return null;
    }

    @Override
    public boolean hasDeserializerFor(DeserializationConfig config, Class<?> valueType) {
        return Result.class.isAssignableFrom(valueType);
    }
}
