package com.paymenthandler.service;

import com.paymenthandler.dao.UserDao;
import com.paymenthandler.model.Role;
import com.paymenthandler.model.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class AuthenticationService {

    @Inject
    private UserDao userDao;

    @Inject
    private PasswordService passwordService;

    public Optional<User> authenticate(String usernameOrEmail, String password) {
        if (usernameOrEmail == null || usernameOrEmail.trim().isEmpty()) {
            return Optional.empty();
        }
        if (password == null || password.isEmpty()) {
            return Optional.empty();
        }
        Optional<User> userOpt = userDao.findByUsernameOrEmail(usernameOrEmail.trim());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPasswordHash() != null &&
                passwordService.verifyPassword(password, user.getPasswordHash())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public User register(String username, String email, String password, Role role) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format");
        }

        passwordService.validatePasswordStrength(password);

        Optional<User> existingUser = userDao.findByEmail(email.trim());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }

        String passwordHash = passwordService.hashPassword(password);
        User newUser = new User(null, username.trim(), email.trim(), passwordHash, role);

        return userDao.createUser(newUser);
    }
}
