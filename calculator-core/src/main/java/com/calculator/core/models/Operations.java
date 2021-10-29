package com.calculator.core.models;

import java.util.stream.Stream;

import static com.calculator.core.utils.CalculatorConstants.INVALID_PRIORITY;
import static com.calculator.core.utils.CalculatorConstants.SPACE;

public enum Operations implements Sign {

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
    UNKNOWN(SPACE, INVALID_PRIORITY);

    private final char symbol;
    private final int priority;

    Operations(char symbol, int priority) {
        this.symbol = symbol;
        this.priority = priority;
    }

    public char getSymbol() {
        return symbol;
    }

    public boolean hasLowerPriority(Operations operation) {
        return Integer.compare(this.getPriority(), operation.getPriority()) < 1;
    }

    public int getPriority() {
        return priority;
    }

    public static Operations findOperation(char symbol) {
        return Stream.of(values())
                .filter(operations -> operations.getSymbol() == symbol)
                .findAny()
                .orElse(UNKNOWN);
    }
}
