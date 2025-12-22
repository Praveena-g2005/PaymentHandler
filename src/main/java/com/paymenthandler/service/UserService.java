package com.paymenthandler.service;

import java.util.*;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.paymenthandler.dao.*;
import com.paymenthandler.model.*;

@ApplicationScoped
public class UserService {

    @Inject
    @Named("jooqUserDao")
    private UserDao dao;

    @Inject
    private AuthenticationService authService;

    public User createUser(String name, String email) {
        Optional<User> existingUser = dao.findByEmail(email);
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Email already exists: " + email);
        }
        return dao.createUser(new User(null, name, email));
    }

    public Optional<User> getUserById(Long id) {
        return dao.findById(id);
    }

    public List<User> findAllUsers() {
        return dao.findAllUsers();
    }

    public List<String> getUsernameStartsWith(char s) {
        List<String> users = dao.findAllUsers().stream().map(User::getName).filter(n -> n.startsWith(String.valueOf(s)))
                .collect(Collectors.toList());
        return users;
    }
    
    public Optional<User> updateUser(Long id, String name, String email){
        return dao.findById(id).map(u-> new User(u.getId(),name,email)).flatMap(dao::updateUser);
    }

    public boolean deleteUser(Long id){
        return dao.deleteUser(id);
    }

    public Optional<User> authenticate(String usernameOrEmail, String password) {
        return authService.authenticate(usernameOrEmail, password);
    }

    public User registerUser(String username, String email, String password) {
        return authService.register(username, email, password, Role.USER);
    }

    public User createUserWithRole(String username, String email, String password, Role role) {
        return authService.register(username, email, password, role);
    }

    public boolean isAdmin(User user) {
        return user != null && user.getRole() == Role.ADMIN;
    }
}
