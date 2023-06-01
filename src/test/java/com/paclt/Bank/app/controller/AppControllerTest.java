package com.paclt.Bank.app.controller;
import org.junit.Assert;
import com.paclt.Bank.app.controller.AppController;
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
import org.junit.Assert;
import org.junit.Test;
import java.util.Arrays;

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
    public void testCalculateAverage() {
        // Test case 1: Average of positive numbers
        int[] numbers = {4, 7, 10, 5, 2};
        double result = AppController.calculateAverage(numbers);
        Assert.assertEquals(5.6, result, 0.001);

        // Test case 2: Average of negative numbers
        numbers = new int[]{-3, -9, -5, -2};
        result = AppController.calculateAverage(numbers);
        Assert.assertEquals(-4.75, result, 0.001);

        // Test case 3: Average of mixed positive and negative numbers
        numbers = new int[]{-1, 2, -4, 6, -3};
        result = AppController.calculateAverage(numbers);
        Assert.assertEquals(0, result, 0.001);
    }
    @Test
    public void testReverseArray() {
        // Test case 1: Reverse an array with odd length
        int[] array = {1, 2, 3, 4, 5};
        AppController.reverseArray(array);
        int[] expectedArray = {5, 4, 3, 2, 1};
        Assert.assertArrayEquals(expectedArray, array);

        // Test case 2: Reverse an array with even length
        array = new int[]{10, 20, 30, 40};
        AppController.reverseArray(array);
        expectedArray = new int[]{40, 30, 20, 10};
        Assert.assertArrayEquals(expectedArray, array);

        // Test case 3: Reverse an empty array
        array = new int[]{};
        AppController.reverseArray(array);
        expectedArray = new int[]{};
        Assert.assertArrayEquals(expectedArray, array);
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

