package com.paymenthandler;

import com.paymenthandler.dao.*;
import com.paymenthandler.model.*;
import com.paymenthandler.service.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.*;

import org.junit.*;

public class UserServiceTest {

    private UserDao dao;
    private UserService userservice;

    @Before
    public void setup() {
        dao = new InMemoryUserDao();        // using in-memory DAO
        userservice = new UserService(dao); // injecting DAO into service
    }

    @Test
    public void createUserTest() {
        User u = userservice.createUser("Praveena", "praveena@gmail.com");

        assertNotNull(u); // user should not be null
        assertEquals("Praveena", u.getName());
        assertEquals("praveena@gmail.com", u.getEmail());
    }

    @Test 
    public void getUserByIdTest() {
        User u = userservice.createUser("Praveena", "praveena@gmail.com");

        Optional<User> user = userservice.getUserById(u.getId());

        assertTrue(user.isPresent());
        assertEquals("Praveena", user.get().getName());
    }

    @After
    public void afterTest() {
        System.out.println("After Test");
    }
}
