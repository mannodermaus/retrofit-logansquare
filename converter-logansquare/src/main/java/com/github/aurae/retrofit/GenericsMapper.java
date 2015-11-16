package com.github.aurae.retrofit;

import java.io.IOException;
import java.io.InputStream;

// TODO Use this interface to generate mapper objects for generics in Retrofit methods
public interface GenericsMapper<T> {

    String serialize(T value) throws IOException;

    T parse(InputStream json) throws IOException;
}
