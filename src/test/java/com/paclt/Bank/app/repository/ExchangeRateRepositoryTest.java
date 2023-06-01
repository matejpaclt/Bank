package com.paclt.Bank.app.repository;

import com.paclt.Bank.app.domain.ExchangeRate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ExchangeRateRepositoryTest {

    @Test
    public void testGetExchangeRates() throws IOException {
        List<ExchangeRate> exchangeRates = getExchangeRates();

        // Verify that the list is not null and contains at least one exchange rate
        assertNotNull(exchangeRates);
        assertFalse(exchangeRates.isEmpty());

        // Additional assertions to check the properties of ExchangeRate objects
        ExchangeRate firstRate = exchangeRates.get(0);
        Assertions.assertEquals("Austrálie", firstRate.getCountry());
        Assertions.assertEquals("dolar", firstRate.getCurrency());
        Assertions.assertEquals("1", firstRate.getAmount());
        Assertions.assertEquals("AUD", firstRate.getCode());
        Assertions.assertEquals("14,394", firstRate.getExchangeRate());

        // Add more assertions as needed for other ExchangeRate objects
    }

    
    @Test
    public void testGetHtmlContent() {
        String url = "http://example.com";
        String content = ExchangeRateRepository.getHtmlContent(url);
        assertNotNull(content);
        assertTrue(content.contains("<html"));
    }

}
