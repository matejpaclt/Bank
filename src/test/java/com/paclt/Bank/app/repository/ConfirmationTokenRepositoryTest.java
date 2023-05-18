package com.paclt.Bank.app.repository;

import com.paclt.Bank.app.domain.ConfirmationToken;
import com.paclt.Bank.app.service.ConfirmationTokenService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ConfirmationTokenRepositoryTest {

    @Test
    public void testSaveConfirmationToken() throws IOException {
        // Create a sample ConfirmationToken object
        String token = "sample-token";
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime expiresAt = LocalDateTime.now().plusDays(1);
        Boolean confirmed = false;
        Long id = 1L;

        ConfirmationToken confirmationToken = new ConfirmationToken(token, createdAt, expiresAt, confirmed, id);

        // Test the saveConfirmationToken method
        ConfirmationTokenService.saveConfirmationToken(confirmationToken);

        // Retrieve the saved token and verify its details
        ConfirmationTokenRepository confirmationTokenRepository = new ConfirmationTokenRepository();
        Optional<ConfirmationToken> savedToken = confirmationTokenRepository.findByToken(token);

        assertFalse(savedToken.isPresent());
    }
    @Test
    public void testGetToken() throws IOException {
        // Create a sample ConfirmationToken object
        String token = "sample-token";
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime expiresAt = LocalDateTime.now().plusDays(1);
        Boolean confirmed = false;
        Long id = 1L;

        ConfirmationToken confirmationToken = new ConfirmationToken(token, createdAt, expiresAt, confirmed, id);
        ConfirmationTokenService.saveConfirmationToken(confirmationToken);

        // Test the getToken method
        ConfirmationTokenService confirmationTokenService = new ConfirmationTokenService(new ConfirmationTokenRepository());
        Optional<ConfirmationToken> retrievedToken = confirmationTokenService.getToken(token);

        // Verify that the token is retrieved and has the correct details
        assertTrue(retrievedToken.isPresent());
    }



    @Test
    public void testSetConfirmedAt() throws IOException {
        File tokensFile = new File("data/tokens.txt");
        if (tokensFile.exists()) {
            if (!tokensFile.delete()) {
                System.err.println("Error deleting tokens.txt file");
                return;
            }
        }
        // Create a sample ConfirmationToken object
        String token = "sample-token";
        LocalDateTime createdAt = LocalDateTime.now().minusDays(1);
        LocalDateTime expiresAt = LocalDateTime.now().plusDays(1);
        Boolean confirmed = false;
        Long id = 1L;

        ConfirmationToken confirmationToken = new ConfirmationToken(token, createdAt, expiresAt, confirmed, id);
        ConfirmationTokenService.saveConfirmationToken(confirmationToken);

        // Test the setConfirmedAt method
        ConfirmationTokenService confirmationTokenService = new ConfirmationTokenService(new ConfirmationTokenRepository());
        int rowsAffected = confirmationTokenService.setConfirmedAt(token);

        // Verify that the confirmation timestamp was set
        assertEquals(1, rowsAffected);

        // Retrieve the updated token and verify the confirmation status
        Optional<ConfirmationToken> updatedToken = confirmationTokenService.getToken(token);
        assertTrue(updatedToken.isPresent());
        assertTrue(updatedToken.get().getConfirmed());
    }

    @Test
    public void testIsTokenConfirmed() throws IOException {
        // Create a sample ConfirmationToken object
        String token = "sample-token";
        LocalDateTime createdAt = LocalDateTime.now().minusDays(1);
        LocalDateTime expiresAt = LocalDateTime.now().plusDays(1);
        Boolean confirmed = true;
        Long id = 1L;

        ConfirmationToken confirmationToken = new ConfirmationToken(token, createdAt, expiresAt, confirmed, id);
        ConfirmationTokenService.saveConfirmationToken(confirmationToken);

        // Test the isTokenConfirmed method
        ConfirmationTokenService confirmationTokenService = new ConfirmationTokenService(new ConfirmationTokenRepository());
        boolean isConfirmed = confirmationTokenService.isTokenConfirmed(token);

        // Verify that the token is confirmed
        assertTrue(isConfirmed);
    }
    private static final String TOKEN_FILE = "data/tokens.txt";
    @Test
    public void testFindByToken_TokenFound() throws IOException {
        String tokenToFind = "token123";
        String existingTokenData = "1," + tokenToFind + ",2023-05-17T10:30:00,2023-05-20T10:30:00,true,1";
        appendDataToFile(existingTokenData);
        ConfirmationTokenRepository repository = new ConfirmationTokenRepository();
        Optional<ConfirmationToken> optionalToken = repository.findByToken(tokenToFind);
        assertTrue(optionalToken.isPresent());
        ConfirmationToken foundToken = optionalToken.get();
        assertEquals(tokenToFind, foundToken.getToken());
        assertEquals(LocalDateTime.of(2023, 5, 17, 10, 30, 00), foundToken.getCreatedAt());
        assertEquals(LocalDateTime.of(2023, 5, 20, 10, 30, 00), foundToken.getExpiresAt());
        assertTrue(foundToken.getConfirmed());
        assertEquals(1L, foundToken.getId());
    }

    private void appendDataToFile(String data) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TOKEN_FILE, true))) {
            writer.write(data);
            writer.newLine();
        }
    }

    @Test
    public void testFindByToken_TokenNotFound() throws IOException {
        String existingTokenData = "1,token123,2023-05-17T10:30:00,2023-05-20T10:30:00,true,1";
        appendDataToFile(existingTokenData);
        String tokenToFind = "nonexistent_token";
        ConfirmationTokenRepository repository = new ConfirmationTokenRepository();
        Optional<ConfirmationToken> optionalToken = repository.findByToken(tokenToFind);
        assertFalse(optionalToken.isPresent());
    }

    @Test
    public void testGetToken_TokenNotFound() throws IOException {
        String existingTokenData = "token123,2023-05-17T10:30:00,2023-05-20T10:30:00,true,1";
        appendDataToFile(existingTokenData);
        String tokenToFind = "nonexistent_token";
        ConfirmationTokenRepository repository = new ConfirmationTokenRepository();
        Optional<ConfirmationToken> optionalToken = repository.getToken(tokenToFind);
        assertFalse(optionalToken.isPresent());
    }
}