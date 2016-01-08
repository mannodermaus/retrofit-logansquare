package com.github.aurae.retrofit2.model;

public enum CustomEnum {
    VAL_1(1),
    VAL_2(2);

    final int val;

    CustomEnum(int val) {
        this.val = val;
    }

    static CustomEnum fromInt(int val) {
        for (CustomEnum customType : values()) {
            if (customType.val == val) return customType;
        }
        throw new AssertionError("custom type with value " + val + " not found.");
    }
}
