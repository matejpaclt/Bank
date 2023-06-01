package com.paclt.Bank.app.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

class UserServiceTest {

    private static final long TEST_USER_ID = 12345;
    private static final String TEST_ACCOUNT_TYPE = "CZK";
    private static final String TEST_LOG_FILE_PATH = "data/log/" + TEST_USER_ID + ".txt";

    @BeforeEach
    public void setUp() throws IOException {
        // Create the log directory
        Path logDirectory = Paths.get("data/log");
        Files.createDirectories(logDirectory);

        // Delete the existing log file, if any
        Path logFilePath = Paths.get(TEST_LOG_FILE_PATH);
        if (Files.exists(logFilePath)) {
            Files.delete(logFilePath);
        }
    }

    @AfterEach
    public void tearDown() throws IOException {
        // Delete the log file after each test
        Path logFilePath = Paths.get(TEST_LOG_FILE_PATH);
        if (Files.exists(logFilePath)) {
            Files.delete(logFilePath);
        }
    }
    private File testAccountFile;


    @Test
    public void testAccountExists_existingAccount_returnsTrue() throws IOException {
        // Create a test account file
        File testAccountFile = new File("data/12345.txt");
        if (!testAccountFile.exists()) {
            testAccountFile.createNewFile();
        }

        // Write test data to the account file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testAccountFile))) {
            writer.write(TEST_ACCOUNT_TYPE + ",100.00");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Call the method
        boolean result = UserService.accountExists(TEST_USER_ID, TEST_ACCOUNT_TYPE);

        // Assert the expected behavior
        Assertions.assertTrue(result, "Account should exist");
    }

    @Test
    public void testAccountExists_nonExistingAccount_throwsException() {
        // Call the method for a non-existing account
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> UserService.accountExists(123, "USD"),
                "Should throw IllegalArgumentException for non-existing account");
    }

    @Test
    public void testAddAccount_existingAccount_throwsException2() {
        // Create a test account file with an existing account
        File testAccountFile = new File("data/3.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testAccountFile))) {
            writer.write("USD,100.00");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Call the method with an existing account type
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> UserService.addAccount(3, "USD", BigDecimal.valueOf(50.00)),
                "Should throw IllegalArgumentException for existing account type");
    }
    @Test
    public void testAddAccount_validAccount_addsAccountSuccessfully() {
        File testAccountFile = new File("data/2.txt");
        // Write test data to the account file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testAccountFile))) {
            writer.write("USD,50.00");
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Call the method
        UserService.addAccount(2, TEST_ACCOUNT_TYPE, BigDecimal.valueOf(100.00));

        // Read the account file and verify the changes
        try (BufferedReader reader = new BufferedReader(new FileReader(testAccountFile))) {
            String line;
            int accountCount = 0;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                Assertions.assertEquals(2, parts.length, "Invalid account file format");
                if (parts[0].equals(TEST_ACCOUNT_TYPE)) {
                    accountCount++;
                    Assertions.assertEquals("CZK", parts[0], "Account type should match");
                    Assertions.assertEquals("100.0", parts[1], "Account amount should match");
                }
            }
            Assertions.assertEquals(1, accountCount, "Account of type should exist");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testPayment_sufficientBalance_returnsOne() throws IOException {
        // Create a test account file with sufficient balance
        File testAccountFile = new File("data/2.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testAccountFile))) {
            writer.write("USD,100.00");
            writer.newLine();
            writer.write("CZK,5000.00");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Call the method
        int result = UserService.payment(2, "USD", 50.00);

        // Assert the expected behavior
        Assertions.assertEquals(1, result, "Payment should succeed");

        // Read the account file and assert the updated balance
        try (BufferedReader reader = new BufferedReader(new FileReader(testAccountFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("USD")) {
                    String[] parts = line.split(",");
                    double updatedBalance = Double.parseDouble(parts[1].trim());
                    Assertions.assertEquals(50.00, updatedBalance, "Incorrect updated balance in USD");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    @Test
    public void testAddAccount_existingAccount_throwsException() {
        File testAccountFile = new File("data/2.txt");
        // Write test data to the account file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testAccountFile))) {
            writer.write(TEST_ACCOUNT_TYPE + ",100.00");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Call the method and assert that it throws an exception
        Assertions.assertThrows(IllegalArgumentException.class, () -> UserService.addAccount(TEST_USER_ID, TEST_ACCOUNT_TYPE, BigDecimal.valueOf(200.00)));
    }
    @Test
    public void testPayment_insufficientBalance_convertToDefaultCurrency_returnsZero() throws IOException {
        // Create a test account file with insufficient balance in both the specified currency (USD) and the default currency (CZK)
        File testAccountFile = new File("data/2.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testAccountFile))) {
            writer.write("USD,10.00");
            writer.newLine();
            writer.write("CZK,100.00");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Call the method
        int result = UserService.payment(2, "USD", 50.00);

        // Assert the expected behavior
        Assertions.assertEquals(0, result, "Payment should fail");

        // Read the account file and assert that the balances remain unchanged
        try (BufferedReader reader = new BufferedReader(new FileReader(testAccountFile))) {
            String line;
            double balanceUSD = 0.00;
            double balanceCZK = 0.00;
            while ((line = reader.readLine()) != null) {
                if (line.contains("USD")) {
                    String[] parts = line.split(",");
                    balanceUSD = Double.parseDouble(parts[1].trim());
                } else if (line.contains("CZK")) {
                    String[] parts = line.split(",");
                    balanceCZK = Double.parseDouble(parts[1].trim());
                }
            }
            Assertions.assertEquals(10.00, balanceUSD, "Incorrect balance in USD");
            Assertions.assertEquals(100.00, balanceCZK, "Incorrect balance in CZK");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    @Test
    public void testDeposit_exceedsMaximumBalance_returnsOne() throws IOException {
        File testAccountFile = new File("data/2.txt");
        // Write test data to the account file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testAccountFile))) {
            writer.write("CZK,9999999.99");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Call the method
        int result = UserService.deposit(2, "CZK", 10000000.00); // Deposit amount exceeds the maximum balance

        // Assert the expected behavior
        Assertions.assertEquals(1, result, "Deposit should fail");
    }

    @Test
    public void testPayment_sufficientBalance_updatesBalanceSuccessfully() throws IOException {
        File testAccountFile = new File("data/2.txt");
        // Write test data to the account file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testAccountFile))) {
            writer.write("Savings,100.00");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Call the method
        int result = UserService.payment(2, TEST_ACCOUNT_TYPE, 50.00);

        // Assert the expected behavior
        Assertions.assertEquals(0, result, "Payment should be successful");

        // Read the account file and verify the updated balance
        try (BufferedReader reader = new BufferedReader(new FileReader(testAccountFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                Assertions.assertEquals(2, parts.length, "Invalid account file format");
                if (parts[0].equals(TEST_ACCOUNT_TYPE)) {
                    Assertions.assertEquals("50.0", parts[1], "Account amount should be updated");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPayment_insufficientBalance_returnsOne() throws IOException {
        File testAccountFile = new File("data/2.txt");
        // Write test data to the account file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testAccountFile))) {
            writer.write("CZK,10.00");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Call the method
        int result = UserService.payment(2, TEST_ACCOUNT_TYPE, 50.00);

        // Assert the expected behavior
        Assertions.assertEquals(0, result, "Payment should fail");
    }

    @Test
    public void testPayment_invalidCurrencyType_returnsZero() throws IOException {
        File testAccountFile = new File("data/2.txt");
        // Write test data to the account file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testAccountFile))) {
            writer.write("Savings,100.00");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Call the method
        int result = UserService.payment(2, "InvalidCurrency", 50.00);

        // Assert the expected behavior
        Assertions.assertEquals(0, result, "Payment should fail");
    }

    @Test
    public void testReadLog_existingLogFile_returnsLogEntriesInReverseOrder() throws IOException {
        // Write test data to the log file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TEST_LOG_FILE_PATH))) {
            writer.write("2023-05-17 09:00 + CZK 50.00\n");
            writer.write("2023-05-16 14:30 - USD 20.00\n");
            writer.write("2023-05-16 12:45 + EUR 100.00\n");
            writer.write("2023-05-15 10:20 + CZK 30.00\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Call the method
        List<String> logEntries = UserService.readLog(TEST_USER_ID);

        // Assert the expected behavior
        Assertions.assertEquals(4, logEntries.size(), "Incorrect number of log entries");

        // Verify the order of log entries
        Assertions.assertEquals("2023-05-15 10:20 + CZK 30.00", logEntries.get(0), "Incorrect log entry");
        Assertions.assertEquals("2023-05-16 12:45 + EUR 100.00", logEntries.get(1), "Incorrect log entry");
        Assertions.assertEquals("2023-05-16 14:30 - USD 20.00", logEntries.get(2), "Incorrect log entry");
        Assertions.assertEquals("2023-05-17 09:00 + CZK 50.00", logEntries.get(3), "Incorrect log entry");
    }
    @Test
    public void testDeposit_validDeposit_updatesBalanceSuccessfully() throws IOException {
        // Create a test account file with an existing account
        File testAccountFile = new File("data/4.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testAccountFile))) {
            writer.write("USD,100.00");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Call the method
        int result = UserService.deposit(4, "USD", 50.00);

        // Assert the expected behavior
        Assertions.assertEquals(1, result, "Deposit should succeed");

        // Read the account file and verify the updated balance
        try (BufferedReader reader = new BufferedReader(new FileReader(testAccountFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("USD")) {
                    String[] parts = line.split(",");
                    double updatedBalance = Double.parseDouble(parts[1].trim());
                    Assertions.assertEquals(150.00, updatedBalance, "Incorrect updated balance after deposit");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeposit_exceedMaxBalance_returnsOne() throws IOException {
        // Create a test account file with an existing account and maximum balance
        File testAccountFile = new File("data/5.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testAccountFile))) {
            writer.write("USD,99999999999999999999999999999.99");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Call the method with an amount that exceeds the maximum balance
        int result = UserService.deposit(5, "USD", 0.01);

        // Assert the expected behavior
        Assertions.assertEquals(1, result, "Deposit should fail");

        // Read the account file and verify the balance remains unchanged
        try (BufferedReader reader = new BufferedReader(new FileReader(testAccountFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("USD")) {
                    String[] parts = line.split(",");
                    double balance = Double.parseDouble(parts[1].trim());
                    Assertions.assertEquals(99999999999999999999999999999.99, balance, "Balance should remain unchanged");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPayment_sufficientBalance_updatesBalancesSuccessfully() throws IOException {
        // Create a test account file with sufficient balances
        File testAccountFile = new File("data/5.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testAccountFile))) {
            writer.write("USD,100.00");
            writer.newLine();
            writer.write("CZK,5000.00");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Call the method
        int result = UserService.payment(5, "USD", 50.00);

        // Assert the expected behavior
        Assertions.assertEquals(1, result, "Payment should succeed");

        // Read the account file and verify the updated balances
        try (BufferedReader reader = new BufferedReader(new FileReader(testAccountFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("USD")) {
                    String[] parts = line.split(",");
                    double updatedBalance = Double.parseDouble(parts[1].trim());
                    Assertions.assertEquals(50.00, updatedBalance, "Incorrect updated USD balance after payment");
                } else if (line.contains("CZK")) {
                    String[] parts = line.split(",");
                    double balance = Double.parseDouble(parts[1].trim());
                    Assertions.assertEquals(5000.00, balance, "CZK balance should remain unchanged");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPayment_insufficientBalance_returnsZeroAndConvertsToDefaultCurrency() throws IOException {
        // Create a test account file with insufficient balances
        File testAccountFile = new File("data/5.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testAccountFile))) {
            writer.write("USD,10.00");
            writer.newLine();
            writer.write("CZK,500.00");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Call the method with an amount that exceeds the USD balance
        int result = UserService.payment(5, "USD", 20.00);

        // Assert the expected behavior
        Assertions.assertEquals(0, result, "Payment should fail");

        // Read the account file and verify the updated balances
        try (BufferedReader reader = new BufferedReader(new FileReader(testAccountFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("USD")) {
                    String[] parts = line.split(",");
                    double balance = Double.parseDouble(parts[1].trim());
                    Assertions.assertEquals(10.00, balance, "USD balance should remain unchanged");
                } else if (line.contains("CZK")) {
                    String[] parts = line.split(",");
                    double balance = Double.parseDouble(parts[1].trim());
                    Assertions.assertEquals(500.00, balance, "Incorrect updated CZK balance after converting payment amount");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testReadLog_nonExistingLogFile_returnsEmptyList() {
        // Call the method
        List<String> logEntries = UserService.readLog(TEST_USER_ID);

        // Assert the expected behavior
        Assertions.assertTrue(logEntries.isEmpty(), "Log entries should be empty");
    }


    @Test
    public void testAccountExists() {
        boolean exists = UserService.accountExists(2, "CZK");
        Assertions.assertTrue(exists);
    }

    @Test
    public void testAccountDoesNotExist() {
        boolean exists = UserService.accountExists(2, "JPY");
        Assertions.assertFalse(exists);
    }


    @Test
    public void testPayment() throws IOException {
        int result = UserService.payment(2, "CZK", 20);
        Assertions.assertEquals(1, result);
    }

    @Test
    public void testPaymentInsufficientFunds() throws IOException {
        int result = UserService.payment(2, "CZK", 1000);
        Assertions.assertEquals(1, result);
    }

    @Test
    public void testReadLog() {
        List<String> log = UserService.readLog(2);
        Assertions.assertNotNull(log);
        Assertions.assertFalse(log.isEmpty());
    }

    @Test
    public void testReadLogNonexistentAccount() {
        List<String> log = UserService.readLog(999);
        Assertions.assertNotNull(log);
        Assertions.assertTrue(log.isEmpty());
    }

    @Test
    public void testAddDuplicateAccount() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            UserService.addAccount(1, "CZK", BigDecimal.valueOf(1000));
        });
    }

    @Test
    public void testPaymentWithConversion() throws IOException {
        int result = UserService.payment(1, "USD", 10);
        Assertions.assertEquals(0, result);
    }




    @Test
    public void testPaymentInvalidAccount() throws IOException {
        int result = UserService.payment(2, "CZK", 50);
        Assertions.assertEquals(1, result);
    }

    @Test
    public void testReadLogFullLog() {
        List<String> log = UserService.readLog(2);
        Assertions.assertNotNull(log);
        Assertions.assertFalse(log.isEmpty());
    }
}


