package com.calculator.core.services;

import com.calculator.core.models.Operations;
import org.apache.commons.lang3.StringUtils;

import static com.calculator.core.utils.CalculatorConstants.Errors.EMPTY_EXPRESSION;
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
        char currentOperation = 0;
        for (char symbol : expression.toCharArray()) {
            if (SPACE == symbol) {
                continue;
            }
            if (Operations.PLUS.symbol == symbol) {
                currentOperation = symbol;
                rpn.append(currentOperand).append(SPACE);
                currentOperand = new StringBuilder();
            } else {
                currentOperand.append(symbol);
            }
        }
        rpn.append(currentOperand).append(SPACE).append(currentOperation).append(SPACE);
        return rpn.toString();
    }
}
