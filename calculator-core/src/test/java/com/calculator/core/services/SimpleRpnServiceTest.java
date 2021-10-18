package com.calculator.core.services;

import org.junit.Test;

import static com.calculator.core.utils.CalculatorConstants.Errors.EMPTY_EXPRESSION;
import static com.calculator.core.utils.CalculatorConstants.INVALID_EXPRESSION_PREFIX;
import static org.junit.Assert.*;

public class SimpleRpnServiceTest {

    //public static final String INVALID_EXPRESSION = "2a + 4 * ( 5 - 5 / 0";
    public static final String SIMPLE_PLUS_EXPRESSION = "2 + 1";
    public static final String COMPLEX_INT_PLUS_EXPRESSION = "21 + 15";
    private RpnService service = new SimpleRpnService();

    @Test
    public void testGetRPNEmptyError() {
        String result = service.getRPN(null);
        assertNotNull("Result can't be null.", result);
        assertTrue("Result should starts with error prefix.", result.startsWith(INVALID_EXPRESSION_PREFIX));
        assertEquals("Result length should be equal to error prefix length.",
                (INVALID_EXPRESSION_PREFIX + EMPTY_EXPRESSION).length(), result.length());
    }

    @Test
    public void testGetRPNSimplePlusOperation() {
        verify(SIMPLE_PLUS_EXPRESSION, "2 1 + ");
    }

    @Test
    public void testGetRPNComplexIntPlusOperation() {
        verify(COMPLEX_INT_PLUS_EXPRESSION, "21 15 + ");
    }

    private void verify(String expression, String expectedValue) {
        String result = service.getRPN(expression);
        assertEquals(expectedValue, result);
    }
}