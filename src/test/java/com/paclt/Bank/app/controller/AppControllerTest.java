package com.paclt.Bank.app.controller;
import org.junit.Assert;
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
@Test
    public void testSum() {
        // Test case 1: Positive numbers
        int result = AppController.sum(5, 10);
        Assert.assertEquals(15, result);

        // Test case 2: Negative numbers
        result = AppController.sum(-8, -3);
        Assert.assertEquals(-11, result);

        // Test case 3: Zero and positive number
        result = AppController.sum(0, 7);
        Assert.assertEquals(7, result);

        // Test case 4: Zero and negative number
        result = AppController.sum(0, -5);
        Assert.assertEquals(-5, result);
    }
    @Test
    public void testBubbleSort() {
        // Test case 1: Sorting an unsorted array
        int[] array = {9, 4, 2, 7, 5};
        AppController.bubbleSort(array);
        int[] expectedArray = {2, 4, 5, 7, 9};
        Assert.assertArrayEquals(expectedArray, array);

        // Test case 2: Sorting an already sorted array
        array = new int[]{1, 2, 3, 4, 5};
        AppController.bubbleSort(array);
        expectedArray = new int[]{1, 2, 3, 4, 5};
        Assert.assertArrayEquals(expectedArray, array);

        // Test case 3: Sorting a large array
        array = new int[]{5, 8, 1, 0, 3, 6, 2, 9, 7, 4};
        AppController.bubbleSort(array);
        expectedArray = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        Assert.assertArrayEquals(expectedArray, array);
    }
    // Add more test methods for other controller methods

}

