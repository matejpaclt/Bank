package com.paclt.Bank.app.controller;

import com.paclt.Bank.app.domain.Account;
import com.paclt.Bank.app.domain.ExchangeRate;
import com.paclt.Bank.app.domain.User;
import com.paclt.Bank.app.repository.AccountRepository;
import com.paclt.Bank.app.repository.ExchangeRateRepository;
import com.paclt.Bank.app.repository.UserRepository;
import com.paclt.Bank.app.service.CustomUserDetailsServiceImpl;
import com.paclt.Bank.app.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.ui.Model;
import static org.mockito.Mockito.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AppControllerTest {
    @Mock
    private CustomUserDetailsServiceImpl customUserDetailsService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ExchangeRateRepository exchangeRateRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private AppController appController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDashboard() throws IOException {
        // Prepare test data
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("test@example.com");

        User user = new User(1L, "John", "Doe", "test@example.com", "password");
        UserRepository userRepositoryMock = spy(UserRepository.class);
        when(userRepositoryMock.getId("test@example.com")).thenReturn(1L);
        when(userRepositoryMock.findUser(1L)).thenReturn(user);

        List<Account> accounts = new ArrayList<>();
        accounts.add(new Account("USD", 100.00));
        AccountRepository accountRepositoryMock = spy(AccountRepository.class);
        when(accountRepositoryMock.findAccountsByUserId(1L)).thenReturn(accounts);

        List<String> logs = new ArrayList<>();
        logs.add("Log entry 1");
        logs.add("Log entry 2");
        UserService userServiceMock = spy(UserService.class);
        when(userServiceMock.readLog(1L)).thenReturn(logs);

        List<ExchangeRate> exchangeRates = new ArrayList<>();
        ExchangeRateRepository exchangeRateRepositoryMock = spy(ExchangeRateRepository.class);
        when(exchangeRateRepositoryMock.getExchangeRates()).thenReturn(exchangeRates);

        // Create the AppController instance

        // Set the mocked dependencies

        // Call the method
        String result = appController.dashboard(mock(Model.class), authentication);

        // Verify the result
        assertEquals("dashboard", result);
        verify(authentication, times(1)).getName();
        verify(userRepositoryMock, times(1)).getId("test@example.com");
        verify(userRepositoryMock, times(1)).findUser(1L);
        verify(accountRepositoryMock, times(1)).findAccountsByUserId(1L);
        verify(userServiceMock, times(1)).readLog(1L);
        verify(exchangeRateRepositoryMock, times(1)).getExchangeRates();
    }




    @Test
    public void testHandleTransaction_withdrawAction_callsHandlePayment() throws IOException {
        // Prepare test data
        BigDecimal amount = new BigDecimal("50.00");
        String accountType = "USD";
        Model model = mock(Model.class);
        Authentication authentication = mock(Authentication.class);

        // Call the method
        String result = appController.handleTransaction("withdraw", amount, accountType, model, authentication);

        // Verify the result
        assertEquals("dashboard", result);
        verify(appController, never()).handleDeposit(any(), any(), any(), any());
        verify(appController, never()).handleOpen(any(), any(), any(), any());
        // Verify that handlePayment was called indirectly
        verify(appController, times(1)).handleTransactionWithdraw(amount, model, accountType, authentication);
    }

    @Test
    public void testHandleTransaction_openAction_callsHandleOpen() throws IOException {
        // Prepare test data
        BigDecimal amount = new BigDecimal("500.00");
        String accountType = "EUR";
        Model model = mock(Model.class);
        Authentication authentication = mock(Authentication.class);

        // Call the method
        String result = appController.handleTransaction("open", amount, accountType, model, authentication);

        // Verify the result
        assertEquals("dashboard", result);
        verify(appController, never()).handleDeposit(any(), any(), any(), any());
        verify(appController, never()).handlePayment(any(), any(), any(), any());
        verify(appController, times(1)).handleOpen(amount, model, accountType, authentication);
    }
    @Test
    public void testViewHomePage() {
        String result = appController.viewHomePage();
        assertEquals("index", result);
    }

    @Test
    public void testShowLoginForm() {
        String result = appController.showLoginForm();
        assertEquals("login", result);
    }

    @Test
    public void testConfirm() {
        Model model = mock(Model.class);
        String token = "exampleToken";
        when(customUserDetailsService.confirmToken(token)).thenReturn(String.valueOf(true));
        String result = appController.confirm(model, token);
        assertEquals("confirm", result);
    }

    // Add more test methods for other controller methods

}

