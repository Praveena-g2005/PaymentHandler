package com.paymenthandler.dao;

import com.paymenthandler.model.*;
import java.util.*;

public interface UserDao {
    User createUser(User user);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameOrEmail(String usernameOrEmail);
    List<User> findAllUsers();
    Optional<User> updateUser(User user);
    boolean deleteUser(Long id);
}
