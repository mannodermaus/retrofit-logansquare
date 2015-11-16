package com.github.aurae.retrofit;

import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.ResponseBody;
import retrofit.Converter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;


/**
 * A {@linkplain Converter.Factory converter} which uses LoganSquare for JSON.
 *
 * @see <a>https://github.com/bluelinelabs/LoganSquare</a>
 */
public final class LoganSquareConverterFactory extends Converter.Factory {
    /**
     * Create an instance. Encoding to JSON and decoding from JSON will use UTF-8.
     * @return A {@linkplain Converter.Factory} configured to serve LoganSquare converters
     */
    public static LoganSquareConverterFactory create() {
        return new LoganSquareConverterFactory();
    }

    private LoganSquareConverterFactory() {
    }

    @Override
    public Converter<ResponseBody, ?> fromResponseBody(Type type, Annotation[] annotations) {
        return new LoganSquareResponseBodyConverter(type);
    }

    @Override
    public Converter<?, RequestBody> toRequestBody(Type type, Annotation[] annotations) {
        return new LoganSquareRequestBodyConverter(type);
    }
}
