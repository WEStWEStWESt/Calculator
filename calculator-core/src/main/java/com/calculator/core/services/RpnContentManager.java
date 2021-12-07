package com.calculator.core.services;

import com.calculator.core.models.Element;
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

    private final StringBuilder rpn;
    private final StringBuilder element;
    private final Deque<Operators> operators;
    private final RpnErrorResolver error;
    private final Statistics statistics;

    RpnContentManager() {
        rpn = new StringBuilder();
        operators = new ArrayDeque<>();
        error = new RpnErrorResolver();
        element = new StringBuilder();
        statistics = new Statistics();
    }

    void resolveSymbol(int code) {
        statistics.incrementLength();
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
            Operands operand = Operands.findOperand(element);
            if (operand.isOperand()) {
                statistics.setLastElement(operand);
                toRpn();
                return;
            }
            error.resolve(element);
        }
    }

    @SuppressWarnings("ALL")
    private void resolveOperator(Operators operator) {
        if (hasNoError()) {
            Element lastElement = statistics.getLastElement();
            if (operator == MINUS && (rpn.isEmpty() || lastElement.isOperation() || lastElement == LEFT_BRACKET)) {
                if (lastElement == UNARY_MINUS) {
                    error.resolve(OPERATIONS_NOT_AGREED);
                } else {
                    operator = UNARY_MINUS;
                }
            }
            statistics.setLastElement(operator);

            if (isPowCascade(operator)) {
                operators.push(operator = MULTIPLY);
                return;
            } else if (operator != POW) statistics.nullifyPowCounter();

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

    private boolean isPowCascade(Operators operator) {
        return operator == POW && statistics.isPowCascade();
    }

    private void resolveElement(char symbol) {
        if (element.isEmpty()) {
            statistics.setCurrentPosition();
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

    private class Statistics {
        private final AtomicInteger length;
        private final AtomicInteger position;
        private final AtomicInteger powCounter;
        private Element lastElement;

        Statistics() {
            this.length = new AtomicInteger();
            this.position = new AtomicInteger();
            this.powCounter = new AtomicInteger();
        }

        Element getLastElement() {
            return lastElement;
        }

        public int getPosition() {
            return position.get();
        }

        void setLastElement(Element lastElement) {
            this.lastElement = lastElement;
        }

        void incrementLength() {
            length.incrementAndGet();
        }

        void nullifyPowCounter() {
            powCounter.set(0);
        }

        boolean isPowCascade() {
            return statistics.powCounter.getAndIncrement() > 0;
        }

        void setCurrentPosition() {
            position.set(length.get());
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
            if (element.isEmpty()) return;
            resolve(OPERAND_ERROR_FORMAT.formatted(element, statistics.getPosition()));
        }

        String get() {
            return message;
        }
    }
}
