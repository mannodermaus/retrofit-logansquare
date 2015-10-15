package com.github.aurae.retrofit;

import com.bluelinelabs.logansquare.JsonMapper;
import com.squareup.okhttp.ResponseBody;
import retrofit.Converter;

import java.io.IOException;
import java.util.Map;

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
