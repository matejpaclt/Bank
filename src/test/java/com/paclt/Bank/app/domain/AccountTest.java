package com.paclt.Bank.app.domain;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AccountTest {

    @Test
    public void testAccountConstructor() {
        // Create an instance of Account using the constructor
        String name = "John Doe";
        double balance = 1000.0;

        Account account = new Account(name, balance);

        // Verify that the Account object is created correctly
        Assertions.assertEquals(name, account.getName());
        Assertions.assertEquals(balance, account.getBalance());
    }

    @Test
    public void testAccountGetters() {
        // Create an instance of Account
        Account account = new Account("John Doe", 1000.0);

        // Test the getter methods
        Assertions.assertEquals("John Doe", account.getName());
        Assertions.assertEquals(1000.0, account.getBalance());
    }
}

