package com.github.aurae.retrofit2.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class GenericModel<T> {

    @JsonField(name = "name")
    String name;

    @JsonField(name = "value")
    T value;

    GenericModel() {
    }

    public GenericModel(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public T getValue() {
        return value;
    }
}
