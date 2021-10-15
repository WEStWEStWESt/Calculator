package com.calculator.core.services;

import org.junit.Test;

import static com.calculator.core.utils.CalculatorConstants.INVALID_EXPRESSION_PREFIX;
import static org.junit.Assert.*;

public class SimpleRpnServiceTest {

    public static final String INVALID_EXPRESSION = "2a + 4 * ( 5 - 5 / 0";
    private RpnService service = new SimpleRpnService();

    @Test
    public void testGetRPNNegativeEmptyError() {
        String result = service.getRPN(null);
        assertNotNull("Result can't be null.", result);
        assertTrue("Result should starts with error prefix.", result.startsWith(INVALID_EXPRESSION_PREFIX));
        assertEquals("Result length should be equal to error prefix length.",
                INVALID_EXPRESSION_PREFIX.length(), result.length());
    }
}