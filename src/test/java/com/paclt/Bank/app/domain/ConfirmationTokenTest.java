package com.paclt.Bank.app.domain;

import com.paclt.Bank.app.domain.ConfirmationToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class ConfirmationTokenTest {

    @Test
    public void testConfirmationToken() {
        // Create a sample ConfirmationToken object
        String token = "sample-token";
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime expiresAt = LocalDateTime.now().plusDays(1);
        Boolean confirmed = false;
        Long id = 1L;

        ConfirmationToken confirmationToken = new ConfirmationToken(token, createdAt, expiresAt, confirmed, id);

        // Test getters
        Assertions.assertEquals(token, confirmationToken.getToken());
        Assertions.assertEquals(createdAt, confirmationToken.getCreatedAt());
        Assertions.assertEquals(expiresAt, confirmationToken.getExpiresAt());
        Assertions.assertEquals(confirmed, confirmationToken.getConfirmed());
        Assertions.assertEquals(id, confirmationToken.getId());

        // Test setters
        LocalDateTime newCreatedAt = LocalDateTime.now().minusDays(1);
        LocalDateTime newExpiresAt = LocalDateTime.now().plusDays(2);
        Boolean newConfirmed = true;
        Long newId = 2L;

        confirmationToken.setToken("new-token");
        confirmationToken.setCreatedAt(newCreatedAt);
        confirmationToken.setExpiresAt(newExpiresAt);
        confirmationToken.setConfirmed(newConfirmed);
        confirmationToken.setId(newId);

        Assertions.assertEquals("new-token", confirmationToken.getToken());
        Assertions.assertEquals(newCreatedAt, confirmationToken.getCreatedAt());
        Assertions.assertEquals(newExpiresAt, confirmationToken.getExpiresAt());
        Assertions.assertEquals(newConfirmed, confirmationToken.getConfirmed());
        Assertions.assertEquals(newId, confirmationToken.getId());
    }
}




