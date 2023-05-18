package com.paclt.Bank.app.security;

import com.paclt.Bank.app.domain.EmailSender;
import com.paclt.Bank.app.service.ConfirmationTokenService;
import com.paclt.Bank.app.service.CustomUserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SecurityConfigTest {
    private SecurityConfig securityConfig;
    private EmailSender emailSender;
    private ConfirmationTokenService confirmationTokenService;

    @BeforeEach
    void setUp() {
        emailSender = mock(EmailSender.class);
        confirmationTokenService = mock(ConfirmationTokenService.class);
        securityConfig = new SecurityConfig(emailSender, confirmationTokenService);
    }

    @Test
    void userDetailsService_ReturnsCustomUserDetailsServiceImpl() {
        UserDetailsService userDetailsService = securityConfig.userDetailsService();
        assertNotNull(userDetailsService);
        assertTrue(userDetailsService instanceof CustomUserDetailsServiceImpl);
    }

    @Test
    void passwordEncoder_ReturnsBCryptPasswordEncoder() {
        BCryptPasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        assertNotNull(passwordEncoder);
    }
    @Test
    public void testConstructor_NoArguments_SetsEmailSenderAndConfirmationTokenServiceToNull() {
        SecurityConfig securityConfig = new SecurityConfig();

        EmailSender emailSender = securityConfig.emailSender;
        ConfirmationTokenService confirmationTokenService = securityConfig.confirmationTokenService;

        assertNull(emailSender);
        assertNull(confirmationTokenService);
    }

}




