package com.github.aurae.retrofit.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

@JsonObject
public class BasicModel {

    @JsonField
    String name;

    @JsonField(typeConverter = CustomEnumConverter.class)
    CustomEnum customType;

    @JsonField(name = "list")
    List<String> values;

    String notSerialized;

    public BasicModel() {

    }

    public BasicModel(String name, String notSerialized, CustomEnum customType, List<String> values) {
        this();
        this.name = name;
        this.notSerialized = notSerialized;
        this.customType = customType;
        this.values = values;
    }

    public String getName() {
        return name;
    }

    public String getNotSerialized() {
        return notSerialized;
    }

    public CustomEnum getCustomType() {
        return customType;
    }

    public List<String> getValues() {
        return values;
    }
}
