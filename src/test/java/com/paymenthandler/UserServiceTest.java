package com.paymenthandler;

import com.paymenthandler.dao.*;
import com.paymenthandler.model.User;
import com.paymenthandler.service.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.*;

import org.junit.*;

public class UserServiceTest {

    private UserDao dao;
    private UserService userservice;

    @Before
    public void setup() throws Exception {
        dao = mock(UserDao.class);        // creating mock object
        userservice = new UserService();  // create service instance

        Field daoField = UserService.class.getDeclaredField("dao");
        daoField.setAccessible(true);
        daoField.set(userservice, dao);
    }

    @Test
    public void createUserTest() {
        //Arrange
        User mockUser= new User(1L,"Praveena", "praveena@gmail.com");
        when(dao.createUser(any(User.class))).thenReturn(mockUser);

        //Act
        User user = userservice.createUser(mockUser.getName(),mockUser.getEmail());

        //Assert
        assertNotNull(user); // user should not be null
        assertEquals("Praveena", user.getName());
        assertEquals("praveena@gmail.com", user.getEmail());

        //Verify
        verify(dao).createUser(any(User.class));
    }

    @Test 
    public void getUserByIdTest() {
        //Arrange
        User mockUser= new User(1L,"Praveena", "praveena@gmail.com");
        when(dao.findById(1L)).thenReturn(Optional.of(mockUser));

        //Act
        Optional<User> user = userservice.getUserById(1L);

        //Assert
        assertTrue(user.isPresent());
        assertEquals("Praveena", user.get().getName());

        //Verify
        verify(dao,times(1)).findById(1L);
    }

    @Test
    public void getUserById_NotFoundtest(){
        //Arrange
        when(dao.findById(77L)).thenReturn(Optional.empty());

        //Act
        Optional<User> user= userservice.getUserById(77L);

        //Assert
        assertTrue(user.isEmpty());

        //Verify
        verify(dao).findById(77L);
    }

    @Test
    public void updateUserNameTest(){
        //Arrange
        User mockUser= new User(1L,"Praveena", "praveena@gmail.com");
        User updatedUser= new User(1L,"Alice", "praveena@gmail.com");
        when(dao.updateUser(any(User.class))).thenReturn(Optional.of(updatedUser));
        when(dao.findById(1L)).thenReturn(Optional.of(mockUser));

        //Act
        Optional<User> user=userservice.updateUserName(1L, "Alice");

        //Asserts
        assertTrue(user.isPresent());
        assertEquals("Alice", user.get().getName());

        //Verify
        verify(dao).findById(1L);
        verify(dao).updateUser(any(User.class));
    }

    @Test
    public void deleteUserTest(){
        //Arrange
        when(dao.deleteUser(1L)).thenReturn(true);

        //Act
        boolean result= userservice.deleteUser(1L);

        //Assert
        assertTrue(result);

        //Verify
        verify(dao).deleteUser(1L);
    }
    @After
    public void afterTest() {
        System.out.println("After Test");
    }
}
