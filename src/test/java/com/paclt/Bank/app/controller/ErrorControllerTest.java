package com.paclt.Bank.app.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ErrorControllerTest {

    @Test
    public void testHandle404Error_Status404_ReturnsErrorPage() {
        ErrorController errorController = new ErrorController();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, 404);

        String result = errorController.handle404Error(request);

        assertEquals("error", result);
    }

    @Test
    public void testHandle404Error_StatusNot404_ReturnsIndexPage() {
        ErrorController errorController = new ErrorController();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, 500);

        String result = errorController.handle404Error(request);

        assertEquals("index", result);
    }

    @Test
    public void testHandle404Error_StatusNull_ReturnsIndexPage() {
        ErrorController errorController = new ErrorController();
        MockHttpServletRequest request = new MockHttpServletRequest();

        String result = errorController.handle404Error(request);

        assertEquals("index", result);
    }
}
