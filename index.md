---
title: Jackson 3.x Datatype Module for Result
description: Result-Jackson3 provides a Jackson 3.x datatype module for Result objects
image: https://dev.leakyabstractions.com/result/result-banner.png
---

# Result Jackson 3.x Module

When using [Result objects][RESULT_REPO] with [Jackson][JACKSON_REPO] we might run into some problems. The
[Jackson 3.x datatype module for Result][RESULT_JACKSON_REPO] solves them by making Jackson treat results as if they
were ordinary objects.

> [Jackson][JACKSON_REPO] is a Java library for [JSON] parsing and generation. It is widely used for converting Java
> objects to JSON and vice versa, making it essential for handling data in web services and RESTful APIs.


## How to Use this Add-On

Add this Maven dependency to your build:

- **Group ID**: `com.leakyabstractions`
- **Artifact ID**: `result-jackson3`
- **Version**: `{{ site.current_version }}`

[Maven Central][ARTIFACTS] provides snippets for different build tools to declare this dependency.


## Test Scenario

Let's start by creating a class `ApiResponse` containing one ordinary and one `Result` field.

```java
{% include_relative result-jackson3/src/test/java/example/ApiResponse.java %}
```

Then we will take a look at what happens when we try to serialize and deserialize `ApiResponse` objects.


## Serializing Results

Next, let's try serializing it using an [object mapper][OBJECT_MAPPER].

```java
{% include_relative result-jackson3/src/test/java/example/Example_Test.java test="serialize_successful_result" %}
```

If we look at the serialized response, we'll see that the `result` field contains a null `failure` value and a
non-null `success` value:

```json
{% include_relative result-jackson3/src/test/resources/serialization_solution_successful_result.json %}
```

Next, we can try serializing a failed result.

```java
{% include_relative result-jackson3/src/test/java/example/Example_Test.java test="serialize_failed_result" %}
```

We can verify that the serialized response contains a non-null `failure` value and a null `success` value.

```json
{% include_relative result-jackson3/src/test/resources/serialization_solution_failed_result.json %}
```


## Deserialization Problem

Now, let's reverse our previous example, this time trying to deserialize a JSON object into an `ApiResponse`.

```java
{% include_relative result-jackson3/src/test/java/example/Fragments.java fragment="deserialize" %}
```

We'll see that we get another `InvalidDefinitionException`. Let's inspect the stack trace.

```
{% include_relative result-jackson3/src/test/resources/deserialization_error.txt %}
```

This behavior again makes sense. Essentially, Jackson cannot create new result objects because `Result` is an interface,
not a concrete type.

```java
{% include_relative result-jackson3/src/test/java/example/Example_Test.java test="deserialization_problem" %}
```


## Solution Implementation

What we want, is for Jackson to treat `Result` values as JSON objects that contain either a `success` or a `failure`
value. Fortunately, there's a Jackson module that can solve this problem.


### Registering the Jackson 3.x Datatype Module for Result

Once we have added Result-Jackson3 as a dependency, all we need to do is register `ResultModule` with our object mapper.

```java
{% include_relative result-jackson3/src/test/java/example/Fragments.java fragment="register_manually" %}
```

Alternatively, you can also make Jackson auto-discover the module.

```java
{% include_relative result-jackson3/src/test/java/example/Fragments.java fragment="register_automatically" %}
```

Regardless of the chosen registration mechanism, once the module is registered all functionality is available for all
normal Jackson operations.


## Deserializing Results

Now, let's repeat our tests for deserialization. If we read our `ApiResponse` again, we'll see that we no longer get an
`InvalidDefinitionException`.

```java
{% include_relative result-jackson3/src/test/java/example/Example_Test.java test="deserialize_successful_result" %}
```

Finally, let's repeat the test again, this time with a failed result. We'll see that yet again we don't get an
exception, and in fact, have a failed result.

```java
{% include_relative result-jackson3/src/test/java/example/Example_Test.java test="deserialize_failed_result" %}
```


## Conclusion

You have learned how to use results with [Jackson][JACKSON_REPO] without any problems by leveraging the
[Jackson 3.x datatype module for Result][RESULT_JACKSON_REPO], demonstrating how it enables Jackson to treat Result
objects as ordinary fields.

The full source code for the examples is [available on GitHub][SOURCE_CODE].


[ARTIFACTS]:                    https://central.sonatype.com/artifact/com.leakyabstractions/result-jackson3/
[JACKSON_LATEST]:               https://search.maven.org/artifact/tools.jackson.core/jackson-core/
[JACKSON_REPO]:                 https://github.com/FasterXML/jackson/
[JSON]:                         https://www.json.org/
[OBJECT_MAPPER]:                https://www.baeldung.com/jackson-object-mapper-tutorial
[RESULT]:                       https://dev.leakyabstractions.com/result/
[RESULT_JACKSON_REPO]:          https://github.com/LeakyAbstractions/result-jackson3/
[RESULT_LATEST]:                https://search.maven.org/artifact/com.leakyabstractions/result/
[RESULT_REPO]:                  https://github.com/LeakyAbstractions/result/
[SOURCE_CODE]:                  https://github.com/LeakyAbstractions/result-jackson3/tree/main/result-jackson3/src/test/java/example
