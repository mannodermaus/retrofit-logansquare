package de.mannodermaus.retrofit2;

import com.bluelinelabs.logansquare.LoganSquare;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import static com.bluelinelabs.logansquare.ConverterUtils.parameterizedTypeOf;

final class LoganSquareRequestBodyConverter implements Converter<Object, RequestBody> {

    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");

    private final Type type;

    LoganSquareRequestBodyConverter(Type type) {
        this.type = type;
    }

    @Override
    public RequestBody convert(Object value) throws IOException {
        // Check for generics
        if (type instanceof java.lang.reflect.ParameterizedType) {
            java.lang.reflect.ParameterizedType pt = (java.lang.reflect.ParameterizedType) type;
            Type rawType = pt.getRawType();
            if (rawType != List.class && rawType != Map.class) {
                return RequestBody.create(MEDIA_TYPE, LoganSquare.serialize(value, parameterizedTypeOf(type)));
            }
        }

        // For general cases, use the central LoganSquare serialization method
        return RequestBody.create(MEDIA_TYPE, LoganSquare.serialize(value));
    }
}
