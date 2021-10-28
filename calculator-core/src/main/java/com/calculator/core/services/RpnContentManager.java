package com.calculator.core.services;

import com.calculator.core.models.Operands;
import com.calculator.core.models.Operations;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayDeque;
import java.util.Deque;

import static com.calculator.core.utils.CalculatorConstants.Errors.OPERAND_ERROR_FORMAT;
import static com.calculator.core.utils.CalculatorConstants.INVALID_EXPRESSION_PREFIX;
import static com.calculator.core.utils.CalculatorConstants.SPACE;

class RpnContentManager {

    private final String expression;
    private final StringBuilder rpn;
    private final StringBuilder element;
    private final Deque<Character> operations;
    private final RpnErrorResolver error;

    RpnContentManager(String expression) {
        this.expression = expression;
        rpn = new StringBuilder();
        operations = new ArrayDeque<>();
        error = new RpnErrorResolver();
        element = new StringBuilder();
    }

    void resolveOperand() {
        if (hasNoError()) {
            if (Operands.isOperand(element)) {
                toRPN();
                return;
            }
            error.resolve(element);
        }
    }

    void resolveOperation(char operation) {
        if (hasNoError()) {
            operations.push(operation);
        }
    }

    void resolveElement(int code) {
        if (SPACE == code) {
            return;
        }
        char symbol = (char) code;
        if (Operations.isOperation(symbol)) {
            resolveOperand();
            resolveOperation(symbol);
        } else {
            element.append(symbol);
        }
    }

    boolean hasNoError() {
        return !hasError();
    }

    boolean hasError() {
        return error.exists();
    }

    boolean hasOperations() {
        return !operations.isEmpty();
    }

    String getRpn() {
        finalizeRpn();
        return hasError() ? error.get() : rpn.toString();
    }

    private void toRPN() {
        rpn.append(element).append(SPACE);
        element.setLength(0);
    }

    private void finalizeRpn() {
        resolveOperand();
        while (hasNoError() && hasOperations()) {
            rpn.append(operations.pop()).append(SPACE);
        }
    }

    private class RpnErrorResolver {

        private String message;

        boolean exists() {
            return StringUtils.isNotBlank(message);
        }

        void resolve(String message) {
            this.message = INVALID_EXPRESSION_PREFIX + message;
        }

        void resolve(StringBuilder element) {
            resolve(OPERAND_ERROR_FORMAT.formatted(element, expression.indexOf(element.toString())));
        }

        String get() {
            return message;
        }
    }
}
