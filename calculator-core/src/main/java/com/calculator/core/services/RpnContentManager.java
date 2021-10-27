package com.calculator.core.services;

import com.calculator.core.models.Operands;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayDeque;
import java.util.Deque;

import static com.calculator.core.utils.CalculatorConstants.Errors.OPERAND_ERROR_FORMAT;
import static com.calculator.core.utils.CalculatorConstants.INVALID_EXPRESSION_PREFIX;
import static com.calculator.core.utils.CalculatorConstants.SPACE;

class RpnContentManager {

    final String expression;
    final StringBuilder rpn;
    final Deque<Character> operations;
    final RpnErrorResolver error;

    RpnContentManager(String expression) {
        this.expression = expression;
        rpn = new StringBuilder();
        operations = new ArrayDeque<>();
        error = new RpnErrorResolver();
    }

    void toRPN(StringBuilder element) {
        rpn.append(element).append(SPACE);
    }

    void resolveOperand(StringBuilder element) {
        if (Operands.isOperand(element)) {
            toRPN(element);
            return;
        }
        error.resolve(element);
    }

    void resolveOperation(char operation) {
        operations.push(operation);
    }

    boolean hasError() {
        return error.exists();
    }

    boolean hasOperations() {
        return !operations.isEmpty();
    }

    String getError() {
        return error.get();
    }

    String getRpn() {
        return rpn.toString();
    }

    void finalizeRpn() {
        while (hasOperations()) {
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
            resolve(String.format(OPERAND_ERROR_FORMAT, element,
                    expression.indexOf(element.toString())));
        }

        String get() {
            return message;
        }
    }
}
