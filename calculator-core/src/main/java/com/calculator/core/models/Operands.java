package com.calculator.core.models;

import java.util.regex.Pattern;
import java.util.stream.Stream;

public enum Operands {
    INTEGER("^[\\d]+$"),
    DOUBLE("^([0]|[1-9]+)[.,][\\d]+$");

    private final Pattern pattern;

    Operands(String regex) {
        pattern = Pattern.compile(regex);
    }

    public static boolean isOperand(StringBuilder value) {
        return Stream.of(values()).anyMatch(instance -> isValid(instance, value));
    }

    private static boolean isValid(Operands operands, StringBuilder value) {
        return operands.pattern.matcher(value).matches();
    }
}
