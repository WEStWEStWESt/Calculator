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
        char currentOperation = 0;
        for (char symbol : expression.toCharArray()) {
            if (Character.isDigit(symbol)) {
                rpn.append(symbol);
            }
            if (Operations.PLUS.symbol == symbol) {
                currentOperation = symbol;
                rpn.append(SPACE);
            }
        }
        rpn.append(SPACE).append(currentOperation).append(SPACE);
        return rpn.toString();
    }
}
