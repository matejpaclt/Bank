package com.paclt.Bank.app.controller;

import com.paclt.Bank.app.controller.AppController;
import com.paclt.Bank.app.service.CustomUserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AppControllerTest {
    @Mock
    private CustomUserDetailsServiceImpl customUserDetailsService;

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
    void testSum() {
        int result = AppController.sum(2, 3);
        assertEquals(5, result);
    }

    @Test
    void testBubbleSort() {
        int[] array = {5, 3, 1, 4, 2};
        AppController.bubbleSort(array);
        int[] expected = {1, 2, 3, 4, 5};
        assertArrayEquals(expected, array);
    }

    @Test
    void testReverseArray() {
        int[] array = {1, 2, 3, 4, 5};
        AppController.reverseArray(array);
        int[] expected = {5, 4, 3, 2, 1};
        assertArrayEquals(expected, array);
    }

    @Test
    void testCalculateAverage() {
        int[] numbers = {1, 2, 3, 4, 5};
        double result = AppController.calculateAverage(numbers);
        assertEquals(3.0, result);
    }
}

