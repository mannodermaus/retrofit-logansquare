package com.github.aurae.retrofit;

import com.bluelinelabs.logansquare.JsonMapper;
import com.bluelinelabs.logansquare.LoganSquare;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

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
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
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
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
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
