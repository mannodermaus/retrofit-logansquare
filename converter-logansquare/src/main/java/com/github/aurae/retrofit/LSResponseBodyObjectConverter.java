package com.github.aurae.retrofit;

import com.bluelinelabs.logansquare.JsonMapper;
import com.squareup.okhttp.ResponseBody;
import retrofit.Converter;

import java.io.IOException;

final class LSResponseBodyObjectConverter<T> implements Converter<ResponseBody, T> {

    private final JsonMapper<T> mapper;

    public LSResponseBodyObjectConverter(JsonMapper<T> mapper) {
        this.mapper = mapper;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        return mapper.parse(value.byteStream());
    }
}
