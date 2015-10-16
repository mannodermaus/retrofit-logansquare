package com.github.aurae.retrofit;

import com.bluelinelabs.logansquare.JsonMapper;
import com.bluelinelabs.logansquare.LoganSquare;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.ResponseBody;
import retrofit.Converter;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;


/**
 * A {@linkplain Converter.Factory converter} which uses LoganSquare for JSON.
 *
 * @see <a>https://github.com/bluelinelabs/LoganSquare</a>
 */
public final class LoganSquareConverterFactory extends Converter.Factory {
    /**
     * Create an instance. Encoding to JSON and decoding from JSON will use UTF-8.
     * @return A {@linkplain Converter.Factory} configured to serve LoganSquare converters
     */
    public static LoganSquareConverterFactory create() {
        return new LoganSquareConverterFactory();
    }

    private LoganSquareConverterFactory() {
    }

    private JsonMapper<?> mapperFor(Type type) {
        return LoganSquare.mapperFor((Class<?>) type);
    }

    @Override
    public Converter<ResponseBody, ?> fromResponseBody(Type type, Annotation[] annotations) {
        if (type instanceof Class) {
            // Return a plain Object converter
            return new LSResponseBodyObjectConverter<>(mapperFor(type));

        } else if (type instanceof ParameterizedType) {
            // Return a List or Map converter
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type[] typeArguments = parameterizedType.getActualTypeArguments();
            Type firstType = typeArguments[0];

            // Check for Map arguments
            if (parameterizedType.getRawType() == Map.class) {
                Type secondType = typeArguments[1];

                // Perform validity checks on the type arguments, since LoganSquare works only on String keys
                if (firstType == String.class && secondType instanceof Class) {
                    // Return a Map converter
                    return new LSResponseBodyMapConverter<>(mapperFor(secondType));
                }

            } else if (firstType instanceof Class) {
                // Return a List converter
                return new LSResponseBodyListConverter<>(mapperFor(firstType));
            }
        }

        // Return null for unsupported types
        return null;
    }

    @Override
    public Converter<?, RequestBody> toRequestBody(Type type, Annotation[] annotations) {
        if (type instanceof Class) {
            // Return a plain Object converter
            return new LSRequestBodyObjectConverter<>(mapperFor(type));

        } else if (type instanceof ParameterizedType) {
            // Return a List or Map converter
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type[] typeArguments = parameterizedType.getActualTypeArguments();
            Type firstType = typeArguments[0];

            // Check for Map arguments
            if (parameterizedType.getRawType() == Map.class) {
                Type secondType = typeArguments[1];

                // Perform validity checks on the type arguments, since LoganSquare works only on String keys
                if (firstType == String.class && secondType instanceof Class) {
                    // Return a Map converter
                    return new LSRequestBodyMapConverter<>(mapperFor(secondType));
                }

            } else if (firstType instanceof Class) {
                // Return a List converter
                return new LSRequestBodyListConverter<>(mapperFor(firstType));
            }
        }

        // Return null for unsupported types
        return null;
    }
}
