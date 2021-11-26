package com.calculator.core.models;

import java.util.stream.Stream;

import static com.calculator.core.utils.CalculatorConstants.INVALID_PRIORITY;
import static com.calculator.core.utils.CalculatorConstants.SPACE;

public enum Operators implements Element {

    LEFT_BRACKET('(', 0) {
        @Override
        public boolean isBracket() {
            return true;
        }
    },
    RIGHT_BRACKET(')', 0) {
        @Override
        public boolean isBracket() {
            return true;
        }
    },
    PLUS('+', 1) {
        @Override
        public boolean isOperation() {
            return true;
        }
    },
    MINUS('-', 1) {
        @Override
        public boolean isOperation() {
            return true;
        }
    },
    MULTIPLY('*', 2) {
        @Override
        public boolean isOperation() {
            return true;
        }
    },
    DIVIDE('/', 2) {
        @Override
        public boolean isOperation() {
            return true;
        }
    },
    UNARY_MINUS('U', 10) {
        @Override
        public boolean isOperation() {
            return true;
        }
    },
    UNKNOWN(SPACE, INVALID_PRIORITY);

    private final char symbol;
    private final int priority;

    Operators(char symbol, int priority) {
        this.symbol = symbol;
        this.priority = priority;
    }

    public char getSymbol() {
        return symbol;
    }

    public boolean hasLowerPriority(Operators operation) {
        return Integer.compare(this.getPriority(), operation.getPriority()) < 1;
    }

    public int getPriority() {
        return priority;
    }

    public boolean isOperator() {
        return isBracket() || isOperation();
    }

    public static Operators findOperation(char symbol) {
        return Stream.of(values())
                .filter(operators -> operators.getSymbol() == symbol)
                .findAny()
                .orElse(UNKNOWN);
    }
}
