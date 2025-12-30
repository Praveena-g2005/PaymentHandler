package com.paymenthandler.web;

import com.google.inject.Provider;
import com.paymenthandler.model.User;
import com.paymenthandler.service.UserService;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

@Singleton
public class AuthenticationServlet extends HttpServlet {

    private final UserService userService;
    private final Provider<UserSession> userSessionProvider;

    @Inject
    public AuthenticationServlet(UserService userService, Provider<UserSession> userSessionProvider) {
        this.userService = userService;
        this.userSessionProvider = userSessionProvider;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        UserSession userSession = userSessionProvider.get();
        String path = req.getServletPath();

        switch (path) {
            case "/auth/login":
                if (userSession.isLoggedIn()) {
                    resp.sendRedirect(req.getContextPath() + "/");
                } else {
                    req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
                }
                break;

            case "/auth/register":
                if (userSession.isLoggedIn()) {
                    resp.sendRedirect(req.getContextPath() + "/");
                } else {
                    req.getRequestDispatcher("/views/register.jsp").forward(req, resp);
                }
                break;

            case "/auth/logout":
                userSession.logout();
                HttpSession session = req.getSession(false);
                if (session != null) {
                    session.invalidate();
                }
                resp.sendRedirect(req.getContextPath() + "/auth/login");
                break;

            default:
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getServletPath();

        switch (path) {
            case "/auth/login":
                handleLogin(req, resp);
                break;

            case "/auth/register":
                handleRegister(req, resp);
                break;

            default:
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void handleLogin(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String usernameOrEmail = req.getParameter("usernameOrEmail");
        String password = req.getParameter("password");

        if (usernameOrEmail == null || usernameOrEmail.trim().isEmpty() ||
                password == null || password.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            req.setAttribute("error", "Username/Email and password are required");
            req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
            return;
        }

        try {
            Optional<User> userOpt = userService.authenticate(usernameOrEmail.trim(), password);

            if (userOpt.isPresent()) {
                UserSession userSession = userSessionProvider.get();
                userSession.setCurrentUser(userOpt.get());
                resp.sendRedirect(req.getContextPath() + "/");
            } else {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                req.setAttribute("error", "Invalid username/email or password");
                req.setAttribute("usernameOrEmail", usernameOrEmail);
                req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            req.setAttribute("error", "Login failed: " + e.getMessage());
            req.setAttribute("usernameOrEmail", usernameOrEmail);
            req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
        }
    }

    private void handleRegister(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");
        String roleStr = req.getParameter("role");

        if (username == null || username.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                password == null || password.isEmpty() ||
                roleStr == null || roleStr.trim().isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            req.setAttribute("error", "All fields are required");
            req.setAttribute("username", username);
            req.setAttribute("email", email);
            req.getRequestDispatcher("/views/register.jsp").forward(req, resp);
            return;
        }

        if (!password.equals(confirmPassword)) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            req.setAttribute("error", "Password doesn't match");
            req.setAttribute("username", username);
            req.setAttribute("email", email);
            req.getRequestDispatcher("/views/register.jsp").forward(req, resp);
            return;
        }

        try {
            com.paymenthandler.model.Role role = com.paymenthandler.model.Role.fromString(roleStr);
            User newUser = userService.createUserWithRole(username.trim(), email.trim(), password, role);
            UserSession userSession = userSessionProvider.get();
            userSession.setCurrentUser(newUser);
            resp.sendRedirect(req.getContextPath() + "/");
        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            req.setAttribute("error", e.getMessage());
            req.setAttribute("username", username);
            req.setAttribute("email", email);
            req.getRequestDispatcher("/views/register.jsp").forward(req, resp);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            req.setAttribute("error", "Registration failed: " + e.getMessage());
            req.setAttribute("username", username);
            req.setAttribute("email", email);
            req.getRequestDispatcher("/views/register.jsp").forward(req, resp);
        }
    }
}
