package com.calculator.core.services;

import org.junit.Test;

import static com.calculator.core.utils.CalculatorConstants.Errors.EMPTY_EXPRESSION;
import static com.calculator.core.utils.CalculatorConstants.INVALID_EXPRESSION_PREFIX;
import static org.junit.Assert.*;

public class LinearRpnServiceTest {

    //public static final String INVALID_EXPRESSION = "2a + 4 * ( 5 - 5 / 0";
    public static final String SIMPLE_PLUS_EXPRESSION = "2 + 1";
    public static final String COMPLEX_PLUS_MINUS_EXPRESSION = "2 + 1 - 52";
    public static final String COMPLEX_INT_PLUS_EXPRESSION = "21 + 15";
    public static final String DOUBLE_PLUS_EXPRESSION = "13.66 + 12.0";
    public static final String DOUBLE_PLUS_INVALID_EXPRESSION = "13.66 + 12..0";
    public static final String LETTER_PLUS_INVALID_EXPRESSION = "13.66a + 12.0";
    public static final String ZERO_PLUS_INVALID_EXPRESSION = "00.35 + 2";
    public static final String SIMPLE_MINUS_EXPRESSION = "2 - 1";
    private RpnService service = new LinearRpnService();

    @Test
    public void testGetRPNEmptyError() {
        String result = service.getRPN(null);
        assertNotNull("Result can't be null.", result);
        assertTrue("Result should starts with error prefix.", result.startsWith(INVALID_EXPRESSION_PREFIX));
        assertEquals("Result length should be equal to error prefix length.",
                (INVALID_EXPRESSION_PREFIX + EMPTY_EXPRESSION).length(), result.length());
    }

    @Test
    public void testGetRPNSimpleMinusOperation() {
        verify(SIMPLE_MINUS_EXPRESSION, "2 1 - ");
    }

    @Test
    public void testGetRPNComplexPlusMinusOperation() {
        verify(COMPLEX_PLUS_MINUS_EXPRESSION, "2 1 + 52 - ");
    }

    @Test
    public void testGetRPNSimplePlusOperation() {
        verify(SIMPLE_PLUS_EXPRESSION, "2 1 + ");
    }

    @Test
    public void testGetRPNComplexIntPlusOperation() {
        verify(COMPLEX_INT_PLUS_EXPRESSION, "21 15 + ");
    }

    @Test
    public void testGetRPNDoublePlusOperation() {
        verify(DOUBLE_PLUS_EXPRESSION, "13.66 12.0 + ");
    }

    @Test
    public void testGetRPNDoublePlusInvalidOperation() {
        verify(DOUBLE_PLUS_INVALID_EXPRESSION,
                INVALID_EXPRESSION_PREFIX + "Operand '12..0' at position 9 invalid.");
    }

    @Test
    public void testGetRPNLetterPlusInvalidOperation() {
        verify(LETTER_PLUS_INVALID_EXPRESSION,
                INVALID_EXPRESSION_PREFIX + "Operand '13.66a' at position 1 invalid.");
    }

    @Test
    public void testGetRPNZeroPlusInvalidOperation() {
        verify(ZERO_PLUS_INVALID_EXPRESSION,
                INVALID_EXPRESSION_PREFIX + "Operand '00.35' at position 1 invalid.");
    }

    private void verify(String expression, String expectedValue) {
        String result = service.getRPN(expression);
        assertEquals(expectedValue, result);
    }
}