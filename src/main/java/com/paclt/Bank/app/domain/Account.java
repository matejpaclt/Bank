package com.paclt.Bank.app.domain;

public class Account {


    private String name;
    private double balance;


    public Account(String name, double balance) {
        this.name = name;
        this.balance = balance;
    }

    public String getName() {
        return name;
    }
    public double getBalance() {
        return Math.round(balance * 1000.0) / 1000.0;
    }

    @Override
    public String toString() {
        return "currency = " + name + '\n' + ", balance = " + balance + '\n' + '}';
    }

}