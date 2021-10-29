package com.calculator.core.models;

public interface Sign {

    default boolean isOperation() {
        return false;
    }
}
