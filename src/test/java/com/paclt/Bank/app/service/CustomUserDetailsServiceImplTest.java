import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import com.paclt.Bank.app.domain.ConfirmationToken;
import com.paclt.Bank.app.domain.EmailSender;
import com.paclt.Bank.app.domain.User;
import com.paclt.Bank.app.repository.UserRepository;
import com.paclt.Bank.app.service.ConfirmationTokenService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class CustomUserDetailsServiceImplTest {

    private EmailSender emailSender;
    private ConfirmationTokenService confirmationTokenService;
    private CustomUserDetailsServiceImpl userDetailsServiceImpl;

    @BeforeEach
    void setUp() {
        emailSender = mock(EmailSender.class);
        confirmationTokenService = mock(ConfirmationTokenService.class);
        userDetailsServiceImpl = new CustomUserDetailsServiceImpl(emailSender, confirmationTokenService);
    }

    @Test
    void loadUserByUsername_ExistingUser_ReturnsCustomUserDetailsService() {
        // Arrange
        String email = "test@example.com";
        User user = new User(1L, "John", "Doe", email, "password");
        when(UserRepository.findUserEmail(email)).thenReturn(user);

        // Mock the behavior of confirmationTokenService
        ConfirmationToken confirmationToken = new ConfirmationToken(
            UUID.randomUUID().toString(),
            LocalDateTime.now(),
            LocalDateTime.now().plusMinutes(15),
            false,
            user.getId()
        );
        when(confirmationTokenService.getToken(any())).thenReturn(Optional.of(confirmationToken));

        // Act
        userDetailsServiceImpl.loadUserByUsername(email);

        // Assert
        // Verify that the email was sent
        verify(emailSender).send(eq(email), anyString());

        // Verify that the confirmationTokenService methods were called
        verify(confirmationTokenService).saveConfirmationToken(any(ConfirmationToken.class));
        verify(confirmationTokenService, atLeastOnce()).getToken(any());
    }

    @Test
    void loadUserByUsername_NonExistingUser_ThrowsUsernameNotFoundException() {
        // Arrange
        String email = "nonexisting@example.com";
        when(UserRepository.findUserEmail(email)).thenReturn(null);

        // Act & Assert
        // Verify that UsernameNotFoundException is thrown
        assertThrows(UsernameNotFoundException.class, () -> userDetailsServiceImpl.loadUserByUsername(email));
    }
}

