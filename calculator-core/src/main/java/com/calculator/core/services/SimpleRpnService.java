package com.calculator.core.services;

import com.calculator.core.models.Operands;
import com.calculator.core.models.Operations;
import org.apache.commons.lang3.StringUtils;

import static com.calculator.core.utils.CalculatorConstants.Errors.EMPTY_EXPRESSION;
import static com.calculator.core.utils.CalculatorConstants.Errors.OPERAND_ERROR_FORMAT;
import static com.calculator.core.utils.CalculatorConstants.INVALID_EXPRESSION_PREFIX;
import static com.calculator.core.utils.CalculatorConstants.SPACE;

public class SimpleRpnService implements RpnService {

    @Override
    public String getRPN(String expression) {
        if (StringUtils.isBlank(expression)) {
            return INVALID_EXPRESSION_PREFIX + EMPTY_EXPRESSION;
        }
        StringBuilder rpn = new StringBuilder();
        StringBuilder currentOperand = new StringBuilder();
        /*

        Deque<Character> operations = new ArrayDeque<>();
        Deque<String> operands = new ArrayDeque<>();

        */
        char currentOperation = 0;
        //    2 + 1 - 52
        for (char symbol : expression.toCharArray()) {
            if (SPACE == symbol) {
                continue;
            }
            if (Operations.isOperation(symbol)) {
                currentOperation = symbol;
                String checked = checkOperand(expression, currentOperand);
                if (isError(checked)) {
                    return checked;
                } else {
                    rpn.append(currentOperand).append(SPACE);
                    currentOperand = new StringBuilder();
                }
            } else {
                currentOperand.append(symbol);
            }
        }
        String checked = checkOperand(expression, currentOperand);
        if (isError(checked)) {
            return checked;
        } else {
            rpn.append(currentOperand).append(SPACE).append(currentOperation).append(SPACE);
        }
        return rpn.toString();
    }

    private String checkOperand(String expression, StringBuilder operand) {
        if (Operands.isOperand(operand)) {
            return operand.toString();
        } else {
            return INVALID_EXPRESSION_PREFIX + String.format(OPERAND_ERROR_FORMAT, operand,
                    expression.indexOf(operand.toString()));
        }
    }

    private boolean isError(String value) {
        return StringUtils.startsWith(value, INVALID_EXPRESSION_PREFIX);
    }
}
