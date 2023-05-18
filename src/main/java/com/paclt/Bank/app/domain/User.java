package com.paclt.Bank.app.domain;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.FileReader;
public class User {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<Account> accounts;

    public User(long id, String firstName, String lastName, String email, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        accounts = new ArrayList<>();
        loadAccountsFromFile("data/" + id + ".txt");
    }

    // getters and setters

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.id).append(" ").append(this.firstName).append(" ").append(this.lastName).append(" ").append(this.email).append(" ").append(this.password);
        return builder.toString();
    }
    public List<Account> getAccounts() {
        return accounts;
    }

    public void loadAccountsFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                Account account = new Account(parts[0], Double.parseDouble(parts[1]));
                accounts.add(account);
            }
        } catch (IOException e) {
            System.err.println("Error loading accounts from file: " + e.getMessage());
        } catch (NullPointerException e) {
            System.err.println("File not found: " + filename);
        }
    }

    public void setId(long id) {
        this.id=id;
    }
}