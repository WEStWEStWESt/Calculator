package com.calculator.core.services;

import org.apache.commons.lang3.StringUtils;

import static com.calculator.core.utils.CalculatorConstants.Errors.EMPTY_EXPRESSION;
import static com.calculator.core.utils.CalculatorConstants.INVALID_EXPRESSION_PREFIX;

public class LinearRpnService implements RpnService {

    @Override
    public String getRPN(String expression) {
        if (StringUtils.isBlank(expression)) {
            return INVALID_EXPRESSION_PREFIX + EMPTY_EXPRESSION;
        }
        var content = new RpnContentManager();
        expression.chars()
                .takeWhile(i -> content.hasNoError())
                .forEach(content::resolveSymbol);
        return content.getRpn();
    }
}
