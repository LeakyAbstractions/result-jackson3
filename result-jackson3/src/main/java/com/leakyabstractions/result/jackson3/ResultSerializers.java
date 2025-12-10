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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.leakyabstractions.result.api.Result;

import tools.jackson.databind.BeanDescription;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.SerializationConfig;
import tools.jackson.databind.ValueSerializer;
import tools.jackson.databind.ser.Serializers;

/**
 * Finds serializers for {@link Result} objects.
 *
 * @author <a href="https://guillermo.dev/">Guillermo Calvo</a>
 */
class ResultSerializers extends Serializers.Base {

    @Override
    public ValueSerializer<?> findSerializer(
            SerializationConfig config,
            JavaType type,
            BeanDescription.Supplier beanDescRef,
            JsonFormat.Value formatOverrides) {
        if (Result.class.isAssignableFrom(type.getRawClass())) {
            return new ResultSerializer(type);
        }
        return null;
    }
}
