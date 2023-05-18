package com.paclt.Bank.app.service;

import com.paclt.Bank.app.domain.ConfirmationToken;
import org.junit.jupiter.api.Assertions;
import com.paclt.Bank.app.domain.EmailSender;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.when;

public class CustomUserDetailsServiceImplTest {

    @Mock
    private EmailSender emailSender;

    @Mock
    private ConfirmationTokenService confirmationTokenService;

    private CustomUserDetailsServiceImpl customUserDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customUserDetailsService = new CustomUserDetailsServiceImpl(emailSender, confirmationTokenService);
    }



    @Test
    void confirmToken_ValidToken_TokenConfirmed() {
        // Arrange
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                false,
                10L);
        when(confirmationTokenService.getToken(token)).thenReturn(Optional.of(confirmationToken));

        String result = customUserDetailsService.confirmToken(token);

        assertEquals("Confirmed", result);
    }

    @Test
    void confirmToken_TokenNotFound_ThrowsIllegalStateException() {
        String token = UUID.randomUUID().toString();
        when(confirmationTokenService.getToken(token)).thenReturn(Optional.empty());
        assertThrows(IllegalStateException.class, () -> customUserDetailsService.confirmToken(token));
    }
    @Test
    public void testLoadUserByUsername_InvalidUsername() {
        EmailSender emailSender = Mockito.mock(EmailSender.class);
        ConfirmationTokenService confirmationTokenService = Mockito.mock(ConfirmationTokenService.class);

        CustomUserDetailsServiceImpl userDetailsService = new CustomUserDetailsServiceImpl(emailSender, confirmationTokenService);

        // Try to load user with an invalid username
        Assertions.assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("invalid@example.com"));
    }

    // Add more test methods if needed
}
