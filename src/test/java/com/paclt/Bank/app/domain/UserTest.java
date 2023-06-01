package com.paclt.Bank.app.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserTest {
    @Test
    public void testToString() {
        // Arrange
        long id = 1;
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        String password = "password";
        User user = new User(id, firstName, lastName, email, password);
        String expectedToString = "1 John Doe john.doe@example.com password";

        // Act
        String actualToString = user.toString();

        // Assert
        assertEquals(expectedToString, actualToString);
    }
    @Test
    public void testSetId() {
        // Create a user with initial ID 1
        User user = new User(1, "John", "Doe", "john.doe@example.com", "password");

        // Verify the initial ID
        assertEquals(1, user.getId());

        // Set a new ID
        long newId = 2;
        user.setId(newId);

        // Verify the updated ID
        assertEquals(newId, user.getId());
    }
    @Test
    public void testGetAccounts() {
        // Create a user with sample account data
        User user = new User(5, "John", "Doe", "john.doe@example.com", "password");

        // Get the accounts
        List<Account> accounts = user.getAccounts();

        // Verify the number of accounts

        // Verify the account details
        assertEquals("CZK", accounts.get(0).getName());
        assertEquals(1000.0, accounts.get(0).getBalance());
        assertEquals("USD", accounts.get(1).getName());
        assertEquals(500.0, accounts.get(1).getBalance());
        assertEquals("EUR", accounts.get(2).getName());
        assertEquals(750.0, accounts.get(2).getBalance());
    }

}

