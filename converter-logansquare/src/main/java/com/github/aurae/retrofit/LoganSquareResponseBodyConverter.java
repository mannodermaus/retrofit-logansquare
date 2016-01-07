package com.github.aurae.retrofit;

import com.bluelinelabs.logansquare.LoganSquare;
import com.squareup.okhttp.ResponseBody;
import retrofit.Converter;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

final class LoganSquareResponseBodyConverter implements Converter<ResponseBody, Object> {

    private final Type type;

    LoganSquareResponseBodyConverter(Type type) {
        this.type = type;
    }

    @Override
    public Object convert(ResponseBody value) throws IOException {
        InputStream is = value.byteStream();
        if (type instanceof Class) {
            // Plain object conversion
            return LoganSquare.parse(is, (Class<?>) type);

        } else if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type[] typeArguments = parameterizedType.getActualTypeArguments();
            Type firstType = typeArguments[0];

            // Check for Map arguments
            Type rawType = parameterizedType.getRawType();
            if (rawType == Map.class) {
                Type secondType = typeArguments[1];

                // Perform validity checks on the type arguments, since LoganSquare works only on String keys
                if (firstType == String.class && secondType instanceof Class) {
                    // Map conversion
                    return LoganSquare.parseMap(is, (Class<?>) secondType);
                }

            } else if (rawType == List.class) {
                if (firstType instanceof Class) {
                    // List conversion
                    return LoganSquare.parseList(is, (Class<?>) firstType);
                }
            } else {
                // TODO Generics
            }
        }
        return null;
    }
}
