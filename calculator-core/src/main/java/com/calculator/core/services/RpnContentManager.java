package com.calculator.core.services;

import com.calculator.core.models.Operands;
import com.calculator.core.models.Operations;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.atomic.AtomicInteger;

import static com.calculator.core.utils.CalculatorConstants.Errors.OPERAND_ERROR_FORMAT;
import static com.calculator.core.utils.CalculatorConstants.INVALID_EXPRESSION_PREFIX;
import static com.calculator.core.utils.CalculatorConstants.SPACE;

class RpnContentManager {

    private final AtomicInteger length;
    private final AtomicInteger position;
    private final StringBuilder rpn;
    private final StringBuilder element;
    private final Deque<Operations> operations;
    private final RpnErrorResolver error;

    RpnContentManager() {
        rpn = new StringBuilder();
        operations = new ArrayDeque<>();
        error = new RpnErrorResolver();
        element = new StringBuilder();
        length = new AtomicInteger();
        position = new AtomicInteger();
    }

    void resolveSymbol(int code) {
        length.incrementAndGet();
        if (SPACE == code) {
            return;
        }
        char symbol = (char) code;
        Operations sign = Operations.findOperation(symbol);
        if (sign.isOperation()) {
            resolveOperand();
            resolveOperation(sign);
        } else {
            resolveElement(symbol);
        }
    }

    boolean hasNoError() {
        return !hasError();
    }

    boolean hasError() {
        return error.exists();
    }

    String getRpn() {
        finalizeRpn();
        return hasError() ? error.get() : rpn.toString();
    }

    private void resolveOperand() {
        if (hasNoError()) {
            if (Operands.isOperand(element)) {
                toRPN();
                return;
            }
            error.resolve(element);
        }
    }

    @SuppressWarnings("ALL")
    private void resolveOperation(Operations operation) {
        if (hasNoError()) {
            while (hasOperations() && operation.hasLowerPriority(operations.peek())){
                toRpn(operations.pop());
            }
            operations.push(operation);
        }
    }

    private void resolveElement(char symbol) {
        if (element.isEmpty()) {
            position.set(length.get());
        }
        element.append(symbol);
    }

    private boolean hasOperations() {
        return !operations.isEmpty();
    }

    private void toRPN() {
        rpn.append(element).append(SPACE);
        element.setLength(0);
    }

    private void toRpn(Operations sign){
        rpn.append(sign.getSymbol()).append(SPACE);
    }

    private void finalizeRpn() {
        resolveOperand();
        while (hasNoError() && hasOperations()) {
            toRpn(operations.pop());
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
            resolve(OPERAND_ERROR_FORMAT.formatted(element, position.get()));
        }

        String get() {
            return message;
        }
    }
}
