package com.github.aurae.retrofit;

import com.bluelinelabs.logansquare.JsonMapper;

import java.io.IOException;

import okhttp3.RequestBody;
import retrofit2.Converter;

import static com.github.aurae.retrofit.LSConverterUtils.MEDIA_TYPE;

final class LSRequestBodyObjectConverter<T> implements Converter<T, RequestBody> {

    private final JsonMapper<T> mapper;

    public LSRequestBodyObjectConverter(JsonMapper<T> mapper) {
        this.mapper = mapper;
    }

    @Override
    public RequestBody convert(T value) throws IOException {
        String serializedValue = mapper.serialize(value);
        return RequestBody.create(MEDIA_TYPE, serializedValue);
    }
}
