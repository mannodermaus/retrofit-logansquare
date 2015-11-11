package com.github.aurae.retrofit;

import com.bluelinelabs.logansquare.LoganSquare;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import retrofit.Converter;

import java.io.IOException;

public class LoganSquareRequestBodyConverter implements Converter<Object, RequestBody> {

    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");

    LoganSquareRequestBodyConverter() {
    }

    @Override
    public RequestBody convert(Object value) throws IOException {
        return RequestBody.create(MEDIA_TYPE, LoganSquare.serialize(value));
    }
}
