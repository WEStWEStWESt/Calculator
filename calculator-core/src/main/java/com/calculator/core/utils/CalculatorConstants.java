package com.calculator.core.utils;

public final class CalculatorConstants {

    private CalculatorConstants() {
    }

    public static final String INVALID_EXPRESSION_PREFIX = "Error: ";
    public static final int INVALID_PRIORITY = 1000;
    public static final char SPACE = ' ';

    public static final class Errors {

        private Errors() {
        }

        public static final String EMPTY_EXPRESSION = "Expression is empty.";

        public static final String OPERAND_ERROR_FORMAT = "Operand '%s' at position %s invalid.";

    }

}
