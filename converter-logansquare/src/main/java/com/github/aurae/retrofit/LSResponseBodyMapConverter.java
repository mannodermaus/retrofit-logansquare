package com.github.aurae.retrofit;

import com.bluelinelabs.logansquare.JsonMapper;

import java.io.IOException;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Converter;

final class LSResponseBodyMapConverter<T> implements Converter<ResponseBody, Map<String, T>> {

	private final JsonMapper<T> mapper;

	public LSResponseBodyMapConverter(JsonMapper<T> mapper) {
		this.mapper = mapper;
	}

	@Override
	public Map<String, T> convert(ResponseBody value) throws IOException {
		return mapper.parseMap(value.byteStream());
	}
}
