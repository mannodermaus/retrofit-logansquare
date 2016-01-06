package com.github.aurae.retrofit;

import com.bluelinelabs.logansquare.JsonMapper;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

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
