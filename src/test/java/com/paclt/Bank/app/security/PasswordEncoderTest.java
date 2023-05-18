package com.paclt.Bank.app.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderTest {

    @Test
    public void testPasswordEncoding() {
        // Create an instance of the password encoder
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Define the raw password
        String rawPassword = "12345";

        // Encode the password
        String encodedPassword = encoder.encode(rawPassword);

        // Verify that the encoded password is not null and not equal to the raw password
        Assertions.assertNotNull(encodedPassword);
        Assertions.assertNotEquals(rawPassword, encodedPassword);
    }
}
