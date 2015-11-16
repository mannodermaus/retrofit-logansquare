package com.github.aurae.retrofit;

import com.bluelinelabs.logansquare.util.SimpleArrayMap;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

final class LoganSquareConverterUtils {

    // TODO Create an annotation processor out of this and statically add all GenericsMapper
    public static final SimpleArrayMap<String, GenericsMapper> GENERIC_MAPPERS = new SimpleArrayMap<>();

    private LoganSquareConverterUtils() {
        // no instances
    }

    static GenericsMapper genericsMapperFor(ParameterizedType parameterizedType) {
        Type rawType = parameterizedType.getRawType();
        Type[] typeArguments = parameterizedType.getActualTypeArguments();
        int typeArgCount = typeArguments.length;
        Type[] types = new Type[typeArgCount + 1];
        types[0] = rawType;
        System.arraycopy(typeArguments, 0, types, 1, typeArgCount);
        return GENERIC_MAPPERS.get(Arrays.toString(types));
    }
}
