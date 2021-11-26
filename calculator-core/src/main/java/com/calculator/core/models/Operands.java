package com.calculator.core.models;

import java.util.regex.Pattern;
import java.util.stream.Stream;

public enum Operands implements Element{
    INTEGER("^[\\d]+$"){
        @Override
        public boolean isOperand() {
            return true;
        }
    },
    DOUBLE("^([0]|[1-9]+)[.,][\\d]+$"){
        @Override
        public boolean isOperand() {
            return true;
        }
    },
    UNKNOWN(" ");

    private final Pattern pattern;

    Operands(String regex) {
        pattern = Pattern.compile(regex);
    }

    private static boolean isValid(Operands operands, StringBuilder value) {
        return operands.pattern.matcher(value).matches();
    }

    public static Operands findOperand(StringBuilder value){
        return Stream.of(values())
                .filter(instance -> isValid(instance, value)).findAny().orElse(UNKNOWN);
    }

}
