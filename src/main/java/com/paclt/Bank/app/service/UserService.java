package com.paclt.Bank.app.service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static com.paclt.Bank.app.service.ExchangeRateService.calculateExchange;

@Service
public class UserService {

    public static boolean accountExists(long id, String type) {
        String filePath = String.format("data/%d.txt", id);
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IllegalArgumentException("Account file not found");
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 2) {
                    throw new IllegalArgumentException("Invalid account file format");
                }
                if (parts[0].equals(type)) {
                    return true;
                }
            }
            return false;
        } catch (IOException e) {
            throw new RuntimeException("Error loading accounts from file", e);
        }
    }

    public static void addAccount(long id, String type, double amount) {
        String filePath = String.format("data/%d.txt", id);
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IllegalArgumentException("Account file not found");
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file));
             BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            String line;
            boolean accountExists = false;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 2) {
                    throw new IllegalArgumentException("Invalid account file format");
                }
                if (parts[0].equals(type)) {
                    accountExists = true;
                    break;
                }
            }
            if (!accountExists) {
                writer.write(type + "," + amount + "\n");
            } else {
                throw new IllegalArgumentException("Account of type already exists");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error loading accounts from file", e);
        }
    }
    public static int deposit(long id, String type, double amount) throws IOException {
        String filePath = String.format("data/%d.txt", id);
        Path inputPath = Paths.get(filePath);

        String tempFilePath = String.format("data/%d_temp.txt", id);
        Path tempPath = Paths.get(tempFilePath);

        double maxBalance = Long.MAX_VALUE; // Maximum balance value

        // Read the existing account balance
        double existingBalance = 0.0;
        boolean foundType = false;

        try (BufferedReader reader = Files.newBufferedReader(inputPath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(type)) {
                    existingBalance = Double.parseDouble(parts[1].trim());
                    foundType = true;
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading account: " + e.getMessage());
            return 0;
        }

        // Check if the deposit amount exceeds the maximum balance
        if (foundType && (existingBalance + amount) > maxBalance) {
            System.err.println("Deposit amount exceeds the maximum balance.");
            return 0;
        }

        // Write updated balance to the temporary file
        try (BufferedReader reader = Files.newBufferedReader(inputPath);
             BufferedWriter writer = Files.newBufferedWriter(tempPath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(type)) {
                    double newAmount = existingBalance + amount;
                    line = type + "," + newAmount;
                }
                writer.write(line + System.lineSeparator());
            }
            writeToLog(id, "+", type, amount);
        } catch (IOException e) {
            System.err.println("Error updating account: " + e.getMessage());
            return 0;
        }

        // Rename the temporary file to the original file
        try {
            Files.move(tempPath, inputPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.err.println("Error renaming file: " + e.getMessage());
            return 0;
        }

        return 1;
    }



    public static int payment(long id, String type, double amount) throws IOException {
        // Define input and temporary files
        File inputFile = new File("data/" + id + ".txt");
        File tempFile = new File("data/" + id + "_temp.txt");
        String found = null;
        double newAmount = 0;
        double balance = 0;
        double czkBalance = 0;

        // Read input file to determine the account balance
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            boolean foundType = false;
            if(type != "CZK") {
                while ((line = reader.readLine()) != null) {
                    if (line.contains("CZK")) {
                        String[] parts = line.split(",");
                        czkBalance = Double.parseDouble(parts[1].trim());
                    }
                    if (line.contains(type)) {
                        String[] parts = line.split(",");
                        balance = Double.parseDouble(parts[1].trim());
                        foundType = true;
                        found = type;
                        if(balance > amount) {
                            newAmount = balance - amount;

                        } else {
                            found = "CZK";
                            amount = calculateExchange(type, amount);
                            newAmount = czkBalance - amount;
                            if(newAmount < 0){
                                return 0;
                            }
                        }
                        break;
                    }
                }
            } else {
                while ((line = reader.readLine()) != null) {
                    if (line.contains(type)) {
                        String[] parts = line.split(",");
                        balance = Double.parseDouble(parts[1].trim());
                        foundType = true;
                        found = "CZK";
                        newAmount = balance - amount;
                        if(newAmount < 0){
                            return 0;
                        }
                        break;
                    }
                }
            }
            reader.close();
            if (!foundType) {
                return 0;
            }

            writeToLog(id, "-", found, amount);
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }

        // Read input file and write to temporary file
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(found)) {
                    line = found + "," + newAmount;
                }
                writer.write(line + System.lineSeparator());
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }

        // Replace the input file with the temporary file
        if (inputFile.delete()) {
            if (!tempFile.renameTo(inputFile)) {
                System.err.println("Error renaming file");
                tempFile.delete(); // Delete the temporary file if renaming fails
                return 0;
            }
        } else {
            System.err.println("Error deleting file");
            tempFile.delete(); // Delete the temporary file if deletion fails
            return 0;
        }

        return 1;
    }




    private static void writeToLog(long id, String type, String currency, double amount) {
        String fileName = "data/log/" + id + ".txt";
        Path filePath = Paths.get(fileName);

        try {
            if (!Files.exists(filePath)) {
                Files.createDirectories(filePath.getParent());
                Files.createFile(filePath);
            }

            List<String> lines = new ArrayList<>(Files.readAllLines(filePath, StandardCharsets.UTF_8));

            if (lines.size() >= 5) {
                lines.remove(0);
            }
            LocalDateTime now = LocalDateTime.now();
            String formattedDateTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

            lines.add(formattedDateTime + " " + type + " " + currency + " " + amount);

            Files.write(filePath, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> readLog(long id) {
        String fileName = "data/log/" + id + ".txt";
        Path filePath = Paths.get(fileName);

        try {
            if (!Files.exists(filePath)) {
                return Collections.emptyList();
            }
            List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
            Collections.reverse(lines);
            return lines;
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public static void main(String[] args) throws IOException {

    }

}


