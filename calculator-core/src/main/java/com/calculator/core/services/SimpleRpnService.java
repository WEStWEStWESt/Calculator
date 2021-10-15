package com.calculator.core.services;

import com.calculator.core.models.Operations;
import com.calculator.core.utils.CalculatorConstants;

public class SimpleRpnService implements RpnService {

    @Override
    public String getRPN(String expression) {

        if (expression == null || expression.length() == 0) {
            return CalculatorConstants.INVALID_EXPRESSION_PREFIX;
        }
        StringBuilder rpn = new StringBuilder();
        char currentOperation = 0;
        for (char symbol : expression.toCharArray()) {
            if (Character.isDigit(symbol)) {
                rpn.append(symbol);
            }
            if (Operations.PLUS.symbol == symbol) {
                currentOperation = symbol;
            }
        }
        rpn.append(currentOperation);
        return rpn.toString();
    }
}
