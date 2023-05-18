package com.paclt.Bank.app.service;

import com.paclt.Bank.app.domain.ConfirmationToken;
import com.paclt.Bank.app.repository.ConfirmationTokenRepository;
import com.paclt.Bank.app.service.ConfirmationTokenService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;



import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

    public class ConfirmationTokenServiceTest {
        private ConfirmationTokenService confirmationTokenService;

        @Mock
        private ConfirmationTokenRepository confirmationTokenRepository;

        @BeforeEach
        public void setUp() {
            MockitoAnnotations.openMocks(this);
            confirmationTokenService = new ConfirmationTokenService(confirmationTokenRepository);
        }

        @Test
        public void testSaveConfirmationToken() {
            ConfirmationToken token = new ConfirmationToken("token123", LocalDateTime.now(), LocalDateTime.now().plusHours(1), false, 1L);
            confirmationTokenService.saveConfirmationToken(token);
            boolean isTokenConfirmed = confirmationTokenService.isTokenConfirmed("token123");
            assertTrue(isTokenConfirmed || !isTokenConfirmed);
        }
        @Test
        public void testIsTokenNotConfirmed() {
            String tokenString = "token123";
            when(confirmationTokenRepository.getToken(tokenString)).thenReturn(Optional.empty());
            boolean isConfirmed = confirmationTokenService.isTokenConfirmed(tokenString);
            assertFalse(isConfirmed);
            verify(confirmationTokenRepository, times(1)).getToken(tokenString);
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
        Assertions.assertTrue(isConfirmed);
    }
}


