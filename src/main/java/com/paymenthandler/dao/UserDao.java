package com.paymenthandler.dao;

import com.paymenthandler.model.*;
import java.util.*;

public interface UserDao {
    User createUser(User user);
    Optional<User> findById(Long id);
    List<User> findAllUsers();
    Optional<User> updateUser(User user);
    boolean deleteUser(Long id);
}
