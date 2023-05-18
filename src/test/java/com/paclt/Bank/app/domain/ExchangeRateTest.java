package com.paclt.Bank.app.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ExchangeRateTest {

    @Test
    public void testExchangeRateConstructor() {
        // Create an instance of ExchangeRate using the constructor
        String country = "United States";
        String currency = "USD";
        String amount = "100";
        String code = "USD";
        String exchangeRate = "1.20";

        ExchangeRate exchangeRateObj = new ExchangeRate(country, currency, amount, code, exchangeRate);

        // Verify that the ExchangeRate object is created correctly
        Assertions.assertEquals(country, exchangeRateObj.getCountry());
        Assertions.assertEquals(currency, exchangeRateObj.getCurrency());
        Assertions.assertEquals(amount, exchangeRateObj.getAmount());
        Assertions.assertEquals(code, exchangeRateObj.getCode());
        Assertions.assertEquals(exchangeRate, exchangeRateObj.getExchangeRate());
    }

    @Test
    public void testExchangeRateGetters() {
        // Create an instance of ExchangeRate
        ExchangeRate exchangeRateObj = new ExchangeRate("United States", "USD", "100", "USD", "1.20");

        // Test the getter methods
        Assertions.assertEquals("United States", exchangeRateObj.getCountry());
        Assertions.assertEquals("USD", exchangeRateObj.getCurrency());
        Assertions.assertEquals("100", exchangeRateObj.getAmount());
        Assertions.assertEquals("USD", exchangeRateObj.getCode());
        Assertions.assertEquals("1.20", exchangeRateObj.getExchangeRate());
    }
}

