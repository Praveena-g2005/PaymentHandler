package com.paymenthandler.web;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Singleton
public class UserSessionProvider implements Provider<UserSession> {
    private static final String SESSION_KEY = "userSession";

    private final Provider<HttpServletRequest> requestProvider;

    @Inject
    public UserSessionProvider(Provider<HttpServletRequest> requestProvider) {
        this.requestProvider = requestProvider;
    }

    @Override
    public UserSession get() {
        try {
            HttpServletRequest request = requestProvider.get();
            HttpSession session = request.getSession(true);

            UserSession userSession = (UserSession) session.getAttribute(SESSION_KEY);
            if (userSession == null) {
                userSession = new UserSession();
                session.setAttribute(SESSION_KEY, userSession);
            }
            return userSession;
        } catch (Exception e) {
            System.err.println("Failed to get UserSession: " + e.getMessage());
            e.printStackTrace();
            throw new IllegalStateException("Cannot access UserSession outside of HTTP request context", e);
        }
    }
}
