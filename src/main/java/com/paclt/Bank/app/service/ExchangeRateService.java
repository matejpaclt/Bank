package com.paclt.Bank.app.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Service;
import static com.paclt.Bank.app.repository.ExchangeRateRepository.getHtmlContent;
import static com.paclt.Bank.app.repository.ExchangeRateRepository.getExchangeRate;

    @Service
    public class ExchangeRateService {

        private final String FILENAME = "exchangeRate.txt";
        private final String FILEPATH = "src/main/resources/";
        private final String URL = "https://www.cnb.cz/cs/financni-trhy/devizovy-trh/kurzy-devizoveho-trhu/kurzy-devizoveho-trhu/denni_kurz.txt";

        public void refreshExchangeFile() throws IOException {
            String htmlContent = getHtmlContent(URL);

            try {
                // Get the current date and time to include in the output file name
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String timestamp = now.format(formatter);

                // Create the file path
                Path filePath = Paths.get(FILEPATH + FILENAME);

                // Check if the file exists, create it if it doesn't
                if (!Files.exists(filePath)) {
                    Files.createFile(filePath);
                }

                // Write the exchange rate data to the file
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile(), false))) {
                    writer.write(timestamp + "\n");
                    writer.write(htmlContent + "\n");
                }
            } catch (IOException e) {
                System.err.println("Error while writing exchange rate file: " + e.getMessage());
            }
        }

    public static double calculateExchange(String currencyFrom, double amount) throws IOException {
        if (currencyFrom.equalsIgnoreCase("CZK")) {
            return amount; // From CZK to CZK
        }

        String[] exchangeInfo = getExchangeRate(currencyFrom);
        double exAmount = Double.parseDouble(exchangeInfo[2].replaceAll(",", ".")); // amount to CZK
        double exRate = Double.parseDouble(exchangeInfo[4].replaceAll(",", ".")); // exchange rate

        return (amount * exRate) / exAmount;
    }


    // Testing
    public static void main(String[] args) throws IOException {
        ExchangeRateService exchangeRateService = new ExchangeRateService();
        exchangeRateService.refreshExchangeFile();
    }
}
