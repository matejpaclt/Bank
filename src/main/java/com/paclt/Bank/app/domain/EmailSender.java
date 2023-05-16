package com.paclt.Bank.app.domain;

public interface EmailSender {
    void send(String to, String email);
}
