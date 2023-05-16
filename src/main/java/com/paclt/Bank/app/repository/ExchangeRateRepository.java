package com.paclt.Bank.app.repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.paclt.Bank.app.domain.ExchangeRate;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.springframework.stereotype.Repository;

@Repository
public class ExchangeRateRepository {

    public static List<ExchangeRate> getExchangeRates() throws IOException {
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

    public static String getHtmlContent(String url) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(url);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            return EntityUtils.toString(response.getEntity());
        } catch (IOException ignored) {

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static void printArray(String[][] text) {
        for (int i = 0; i < text.length; i++) {
            for (int j = 0; j < text[i].length; j++) {
                System.out.print(text[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static String[] getExchangeRate(String currency) throws IOException {
        String[][] read = readExchangeFile();
        for (int i = 0; i < read.length; i++) {
            if (read[i][3].equalsIgnoreCase(currency)) {
                return read[i];
            }
        }
        return null;
    }

    public static String[][] readExchangeFile() throws IOException {
        List<String> edit = Files.readAllLines(Paths.get("src/main/resources/ExchangeRate.txt"));
        List<String[]> outputList = new ArrayList<>();

        for (int i = 3; i < edit.size(); i++) {
            String line = edit.get(i).trim();
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




    public static void main(String[] args) throws IOException {
        printArray(readExchangeFile());

    }
}