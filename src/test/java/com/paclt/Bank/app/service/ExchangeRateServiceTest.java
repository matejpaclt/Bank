package com.paclt.Bank.app.service;


import org.junit.jupiter.api.Test;

import java.io.IOException;

public class ExchangeRateServiceTest {

    @Test
    public void testRefreshExchangeFile() throws IOException {
        // Create an instance of the ExchangeRateService
        ExchangeRateService exchangeRateService = new ExchangeRateService();

        // Test if the exchange rate file can be refreshed
        exchangeRateService.refreshExchangeFile();
    }
/*
    @Test
    public void testCalculateExchange() throws IOException {
        // Test if the exchange calculation is correct
        String currencyFrom = "USD";
        double amount = 100.0;
        double calculatedAmount = ExchangeRateService.calculateExchange(currencyFrom, amount);

        // Verify that the calculated amount is as expected
        // Add assertions for the expected calculated amount
    }*/
}

