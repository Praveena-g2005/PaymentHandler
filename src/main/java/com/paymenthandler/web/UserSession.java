package com.paymenthandler.web;

import com.paymenthandler.model.User;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Session-scoped bean for managing user session data
 * Demonstrates @SessionScoped - one instance per HTTP session
 *
 * Compare with @ApplicationScoped (one instance for entire app)
 */
@Named("userSession")
@SessionScoped
public class UserSession implements Serializable {
    private static final long serialVersionUID = 1L;

    private User currentUser;
    private boolean loggedIn;
    private int pageViews;

    public UserSession() {
        this.pageViews = 0;
        System.out.println("New UserSession created for HTTP session");
    }

    // Getters and setters
    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        this.loggedIn = (user != null);
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void logout() {
        this.currentUser = null;
        this.loggedIn = false;
    }

    public String getUserDisplayName() {
        return currentUser != null ? currentUser.getName() : "Guest";
    }

    public Long getCurrentUserId() {
        return currentUser != null ? currentUser.getId() : null;
    }

    // Track page views per session (demonstrates session state)
    public void incrementPageViews() {
        this.pageViews++;
    }

    public int getPageViews() {
        return pageViews;
    }
}
