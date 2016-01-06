package com.github.aurae.retrofit;

import com.bluelinelabs.logansquare.JsonMapper;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Converter;

final class LSResponseBodyListConverter<T> implements Converter<ResponseBody, List<T>> {

	private final JsonMapper<T> mapper;

	public LSResponseBodyListConverter(JsonMapper<T> mapper) {
		this.mapper = mapper;
	}

	@Override
	public List<T> convert(ResponseBody value) throws IOException {
		return mapper.parseList(value.byteStream());
	}
}
