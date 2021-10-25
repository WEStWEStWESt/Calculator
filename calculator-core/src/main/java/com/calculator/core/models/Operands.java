package com.calculator.core.models;

import java.util.regex.Pattern;
import java.util.stream.Stream;

public enum Operands {
    INTEGER("^[\\d]+$"),
    DOUBLE("^[\\d]+[.,][\\d]+$");//TODO the first part of the number should start from one zero only !!!

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
