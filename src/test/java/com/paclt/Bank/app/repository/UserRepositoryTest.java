package com.paclt.Bank.app.repository;


import com.paclt.Bank.app.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserRepositoryTest {

    @Test
    public void testFindUser() {
        User user = UserRepository.findUser(1);
        assertNotNull(user);
        assertEquals(1, user.getId());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals("$2a$10$hFUeuS1gRvkMKYLS3OHBoeO0Jbu1ZwB9T8NDupwttZV4NZsnVBWNW", user.getPassword());
    }

    @Test
    public void testGetId() {
        long id = UserRepository.getId("john.doe@example.com");
        assertEquals(1, id);
    }

    @Test
    public void testFindUserEmail() {
        User user = UserRepository.findUserEmail("john.doe@example.com");
        assertNotNull(user);
        assertEquals(1, user.getId());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals("$2a$10$hFUeuS1gRvkMKYLS3OHBoeO0Jbu1ZwB9T8NDupwttZV4NZsnVBWNW", user.getPassword());
    }
    @Test
    public void testFindUser_UserExists() {
        long id = 1;
        User user = UserRepository.findUser(id);
        assertNotNull(user);
        assertEquals(id, user.getId());
    }

    @Test
    public void testFindUser_UserNotExists() {
        long id = 100;
        User user = UserRepository.findUser(id);
        assertNull(user);
    }

    @Test
    public void testGetId_UserExists() {
        String email = "jane.doe@example.com";
        long id = UserRepository.getId(email);
        assertNotEquals(0, id);
    }

    @Test
    public void testGetId_UserNotExists() {
        String email = "nonexistent@example.com";
        long id = UserRepository.getId(email);
        assertEquals(0, id);
    }

    @Test
    public void testFindUserEmail_UserExists() {
        String email = "jane.doe@example.com";
        User user = UserRepository.findUserEmail(email);
        assertNotNull(user);
        assertEquals(email, user.getEmail());
    }

    @Test
    public void testFindUserEmail_UserNotExists() {
        String email = "nonexistent@example.com";
        User user = UserRepository.findUserEmail(email);
        assertNull(user);
    }
}
