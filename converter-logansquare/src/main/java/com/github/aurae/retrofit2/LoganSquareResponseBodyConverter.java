package com.github.aurae.retrofit2;

import com.bluelinelabs.logansquare.LoganSquare;
import okhttp3.ResponseBody;
import retrofit2.Converter;

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
        try {
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
                    return LoganSquare.parseMap(is, (Class<?>) typeArguments[1]);

                } else if (rawType == List.class) {
                    return LoganSquare.parseList(is, (Class<?>) firstType);

                } else {
                    // TODO Generics
                }
            }
            return null;

        } finally {
            // Close the response body after being done with it
            value.close();
        }
    }
}
