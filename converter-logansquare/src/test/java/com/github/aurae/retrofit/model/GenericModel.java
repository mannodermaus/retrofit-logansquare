package com.github.aurae.retrofit.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class GenericModel<T> {

    @JsonField
    public String name;

    @JsonField
    public T content;
}
