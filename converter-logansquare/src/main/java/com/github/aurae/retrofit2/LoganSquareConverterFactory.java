package com.github.aurae.retrofit2;

import com.bluelinelabs.logansquare.LoganSquare;
import com.bluelinelabs.logansquare.ParameterizedType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;


/**
 * A {@linkplain Converter.Factory converter} which uses LoganSquare for JSON.
 *
 * @see <a>https://github.com/bluelinelabs/LoganSquare</a>
 */
public final class LoganSquareConverterFactory extends Converter.Factory {
    /**
     * Create an instance. Encoding to JSON and decoding from JSON will use UTF-8.
     *
     * @return A {@linkplain Converter.Factory} configured to serve LoganSquare converters
     */
    public static LoganSquareConverterFactory create() {
        return new LoganSquareConverterFactory();
    }

    private LoganSquareConverterFactory() {
    }

    private boolean isSupported(Type type) {
        // Check ordinary Class
        if (type instanceof Class && !LoganSquare.supports((Class) type)) {
            return false;
        }

        // Check LoganSquare's ParameterizedType
        if (type instanceof ParameterizedType && !LoganSquare.supports((ParameterizedType) type)) {
            return false;
        }

        // Check target types of java.lang.reflect.ParameterizedType
        if (type instanceof java.lang.reflect.ParameterizedType) {
            java.lang.reflect.ParameterizedType pt = (java.lang.reflect.ParameterizedType) type;
            Type[] typeArguments = pt.getActualTypeArguments();
            Type firstType = typeArguments[0];

            Type rawType = pt.getRawType();
            if (rawType == Map.class) {
                // LoganSquare only handles Map objects with String keys and supported types
                Type secondType = typeArguments[1];
                if (firstType != String.class || !isSupported(secondType)) {
                    return false;
                }

            } else if (rawType == List.class) {
                // LoganSquare only handles List objects of supported types
                if (!isSupported(firstType)) {
                    return false;
                }

            } else {
                // TODO Generics
            }
        }

        return true;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        if (isSupported(type)) {
            return new LoganSquareResponseBodyConverter(type);
        } else {
            return null;
        }
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        if (isSupported(type)) {
            return new LoganSquareRequestBodyConverter(type);
        } else {
            return null;
        }
    }
}
