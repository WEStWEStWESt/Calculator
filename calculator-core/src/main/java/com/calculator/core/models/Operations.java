package com.calculator.core.models;

import java.util.stream.Stream;

public enum Operations {
    PLUS('+'),
    MINUS('-');

    private final char symbol;

    public char getSymbol() {
        return symbol;
    }

    Operations(char symbol) {
        this.symbol = symbol;
    }

    public static boolean isOperation(char symbol) {
        return Stream.of(values()).map(Operations::getSymbol).anyMatch(value -> value == symbol);
    }
}
