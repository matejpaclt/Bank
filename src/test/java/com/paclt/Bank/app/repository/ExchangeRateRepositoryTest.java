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
        Assertions.assertEquals("14,529", firstRate.getExchangeRate());

        // Add more assertions as needed for other ExchangeRate objects
    }

    @Test
    public void testGetExchangeRates2() throws IOException {
        List<ExchangeRate> exchangeRates = ExchangeRateRepository.getExchangeRates();
        assertNotNull(exchangeRates);
        assertFalse(exchangeRates.isEmpty());
    }

    private List<ExchangeRate> getExchangeRates() throws IOException {
        List<ExchangeRate> exchangeRates = new ArrayList<>();

        String[][] exchangeRateArray = readExchangeFile();

        for (String[] exchangeRate : exchangeRateArray) {
            ExchangeRate exRate = new ExchangeRate(
                    exchangeRate[0],
                    exchangeRate[1],
                    exchangeRate[2],
                    exchangeRate[3],
                    exchangeRate[4]
            );
            exchangeRates.add(exRate);
        }
        return exchangeRates;
    }

    private String[][] readExchangeFile() throws IOException {
        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get("src/main/resources/exchangeRate.txt"), StandardCharsets.ISO_8859_1);
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
            throw e;
        }

        List<String[]> outputList = new ArrayList<>();

        for (int i = 3; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (!line.isEmpty()) {
                String[] parts = line.split("\\|");
                if (parts.length == 5) {
                    outputList.add(parts);
                } else {
                    System.err.println("Invalid line format: " + line);
                }
            }
        }

        String[][] output = new String[outputList.size()][];
        for (int i = 0; i < outputList.size(); i++) {
            output[i] = outputList.get(i);
        }

        return output;
    }

    @Test
    public void testGetHtmlContent() {
        String url = "http://example.com";
        String content = ExchangeRateRepository.getHtmlContent(url);
        assertNotNull(content);
        assertTrue(content.contains("<html"));
    }

    @Test
    public void testPrintExchangeRates() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        ExchangeRateRepository.printExchangeRates();
        String output = outputStream.toString();
        assertNotNull(output);
        assertTrue(output.contains("Exchange rates:"));
    }
}
