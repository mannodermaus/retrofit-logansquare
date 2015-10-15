package com.github.aurae.retrofit;

import com.bluelinelabs.logansquare.JsonMapper;
import com.squareup.okhttp.ResponseBody;
import retrofit.Converter;

import java.io.IOException;
import java.util.List;

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
