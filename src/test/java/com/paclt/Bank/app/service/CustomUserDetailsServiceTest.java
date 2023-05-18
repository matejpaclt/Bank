package com.paclt.Bank.app.service;

import com.paclt.Bank.app.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CustomUserDetailsServiceTest {


    @Test
    public void testGetPassword() {
        User user = new User(1, "John", "Doe", "john.doe@example.com", "password");
        CustomUserDetailsService userDetailsService = new CustomUserDetailsService(user);
        String password = userDetailsService.getPassword();
        Assertions.assertEquals("password", password);
    }


}

