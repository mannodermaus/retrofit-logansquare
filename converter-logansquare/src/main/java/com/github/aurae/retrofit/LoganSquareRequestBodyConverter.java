package com.github.aurae.retrofit;

import com.bluelinelabs.logansquare.LoganSquare;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import retrofit.Converter;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

final class LoganSquareRequestBodyConverter implements Converter<Object, RequestBody> {

    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");

    private final Type type;

    LoganSquareRequestBodyConverter(Type type) {
        this.type = type;
    }

    @Override
    public RequestBody convert(Object value) throws IOException {
        if (type instanceof ParameterizedType) {
            // Check for generics types
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type rawType = parameterizedType.getRawType();
            if (rawType != List.class && rawType != Map.class) {
                // TODO Generics
            }
        }

        // For general cases, use the central LoganSquare serialization method
        return RequestBody.create(MEDIA_TYPE, LoganSquare.serialize(value));
    }
}
