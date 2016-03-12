package com.bluelinelabs.logansquare;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Utility class for the {@link com.github.aurae.retrofit2.LoganSquareConverterFactory}. This resides in LoganSquare's
 * main package in order to take advantage of the package-visible ConcreteParameterizedType class, which is essential
 * to the support of generic classes in the Retrofit converter.
 */
public final class ConverterUtils {

    private ConverterUtils() {
        throw new AssertionError();
    }

    public static boolean isSupported(Type type) {
        // Check ordinary Class
        if (type instanceof Class && !LoganSquare.supports((Class) type)) {
            return false;
        }

        // Check LoganSquare's ParameterizedType
        if (type instanceof ParameterizedType && !LoganSquare.supports((ParameterizedType) type)) {
            return false;
        }

        // Check target types of java.lang.reflect.ParameterizedType
        if (type instanceof java.lang.reflect.ParameterizedType) {
            java.lang.reflect.ParameterizedType pt = (java.lang.reflect.ParameterizedType) type;
            Type[] typeArguments = pt.getActualTypeArguments();
            Type firstType = typeArguments[0];

            Type rawType = pt.getRawType();
            if (rawType == Map.class) {
                // LoganSquare only handles Map objects with String keys and supported types
                Type secondType = typeArguments[1];
                if (firstType != String.class || !isSupported(secondType)) {
                    return false;
                }

            } else if (rawType == List.class) {
                // LoganSquare only handles List objects of supported types
                if (!isSupported(firstType)) {
                    return false;
                }

            } else {
                // Check for generics
                return LoganSquare.supports(parameterizedTypeOf(type));
            }
        }

        return true;
    }

    public static ParameterizedType parameterizedTypeOf(Type type) {
        return new ParameterizedType.ConcreteParameterizedType(type);
    }
}
