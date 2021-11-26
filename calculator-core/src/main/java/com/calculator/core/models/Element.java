package com.calculator.core.models;

public interface Element {

    default boolean isOperation() {
        return false;
    }

    default boolean isBracket() {
        return false;
    }

    default boolean isOperand(){
        return false;
    }
}
