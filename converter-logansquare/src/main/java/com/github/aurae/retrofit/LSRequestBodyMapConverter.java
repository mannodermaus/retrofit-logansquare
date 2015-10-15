package com.github.aurae.retrofit;

import com.bluelinelabs.logansquare.JsonMapper;
import com.squareup.okhttp.RequestBody;
import retrofit.Converter;

import java.io.IOException;
import java.util.Map;

import static com.github.aurae.retrofit.LSConverterUtils.MEDIA_TYPE;

final class LSRequestBodyMapConverter<T> implements Converter<Map<String, T>, RequestBody> {

    private final JsonMapper<T> mapper;

    public LSRequestBodyMapConverter(JsonMapper<T> mapper) {
        this.mapper = mapper;
    }

    @Override
    public RequestBody convert(Map<String, T> value) throws IOException {
        String serializedValue = mapper.serialize(value);
        return RequestBody.create(MEDIA_TYPE, serializedValue);
    }
}
