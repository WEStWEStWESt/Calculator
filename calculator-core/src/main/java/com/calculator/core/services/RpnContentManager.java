package com.calculator.core.services;

import com.calculator.core.models.Operands;
import com.calculator.core.models.Operators;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.atomic.AtomicInteger;

import static com.calculator.core.models.Operators.*;
import static com.calculator.core.utils.CalculatorConstants.Errors.OPERAND_ERROR_FORMAT;
import static com.calculator.core.utils.CalculatorConstants.Errors.OPERATIONS_NOT_AGREED;
import static com.calculator.core.utils.CalculatorConstants.INVALID_EXPRESSION_PREFIX;
import static com.calculator.core.utils.CalculatorConstants.SPACE;

class RpnContentManager {

    private final AtomicInteger length;
    private final AtomicInteger position;
    private final StringBuilder rpn;
    private final StringBuilder element;
    private final Deque<Operators> operators;
    private final RpnErrorResolver error;

    RpnContentManager() {
        rpn = new StringBuilder();
        operators = new ArrayDeque<>();
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
        Operators sign = Operators.findOperation(symbol);
        if (sign.isOperator()) {
            resolveOperand();
            resolveOperator(sign);
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
                toRpn();
                return;
            }
            error.resolve(element);
        }
    }

    //     3 + ( -1 - ---2 )
    @SuppressWarnings("ALL")
    private void resolveOperator(Operators operator) {
        if (hasNoError()) {
            if (operator == MINUS && rpn.isEmpty()) {
                if (operators.peek() == UNARY_MINUS) {
                    error.resolve(OPERATIONS_NOT_AGREED);
                } else {
                    operator = UNARY_MINUS;
                }
            }

            if (!rpn.isEmpty() && operators.peek() == operator){
                operator = UNARY_MINUS;
            }

            if (operator == RIGHT_BRACKET) {
                while (hasOperators() && (operator = operators.pop()) != LEFT_BRACKET) {
                    toRpn(operator);
                }
            } else {
                while (operator.isOperation() && hasOperators() && operator.hasLowerPriority(operators.peek())) {
                    toRpn(operators.pop());
                }
                operators.push(operator);
            }
        }
    }

    private void resolveElement(char symbol) {
        if (element.isEmpty()) {
            position.set(length.get());
        }
        element.append(symbol);
    }

    private boolean hasOperators() {
        return !operators.isEmpty();
    }

    private void toRpn() {
        rpn.append(element).append(element.isEmpty() ? StringUtils.EMPTY : SPACE);
        element.setLength(0);
    }

    private void toRpn(Operators sign) {
        rpn.append(sign.getSymbol()).append(SPACE);
    }

    private void finalizeRpn() {
        resolveOperand();
        while (hasNoError() && hasOperators()) {
            toRpn(operators.pop());
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
