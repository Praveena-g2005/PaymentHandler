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
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Singleton
public class UserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final UserService userService;
    private final Provider<UserSession> userSessionProvider;

    @Inject
    public UserServlet(UserService userService, Provider<UserSession> userSessionProvider) {
        this.userService = userService;
        this.userSessionProvider = userSessionProvider;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        UserSession userSession = userSessionProvider.get();
        System.out.println(">>> userSession = " + userSession);
        System.out.println(">>> userService = " + userService);

        userSession.incrementPageViews();

        String pathInfo = req.getPathInfo();
        System.out.println(">>> UserServlet: pathInfo = " + pathInfo);
        System.out.println(">>> UserServlet: requestURI = " + req.getRequestURI());
        System.out.println(">>> UserServlet: servletPath = " + req.getServletPath());

        if (pathInfo == null || pathInfo.equals("/")) {
            if (!userSession.isAdmin()) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN,
                        "Access denied, only admin can access");
                return;
            }
            List<User> users = userService.findAllUsers();
            req.setAttribute("users", users);
            req.getRequestDispatcher("/views/users.jsp").forward(req, resp);
        } else {
            try {
                Long userId = Long.parseLong(pathInfo.substring(1));
                System.out.println(">>> Looking for user ID: " + userId);

                if (!userSession.isAdmin() && !userId.equals(userSession.getCurrentUserId())) {
                    System.out.println(">>> Access denied - not admin and not own profile");
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN,
                            "Access denied. You can only view your own profile.");
                    return;
                }

                Optional<User> user = userService.getUserById(userId);
                System.out.println(">>> User found: " + user.isPresent());

                if (user.isPresent()) {
                    System.out.println(">>> Forwarding to user-detail.jsp for user: " + user.get().getName());
                    req.setAttribute("user", user.get());
                    req.getRequestDispatcher("/views/user-detail.jsp").forward(req, resp);
                    System.out.println(">>> Forward completed");
                } else {
                    System.out.println(">>> User not found, returning 404");
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
                }
            } catch (NumberFormatException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            UserSession userSession = userSessionProvider.get();
            String action = req.getParameter("action");

            if ("create".equals(action)) {
                if (!userSession.isAdmin()) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN,
                            "Access denied. Admin privileges required.");
                    return;
                }
                try {
                    String name = req.getParameter("name");
                    String email = req.getParameter("email");
                    User user = userService.createUser(name, email);
                    resp.sendRedirect(req.getContextPath() + "/users/" + user.getId());
                } catch (IllegalArgumentException e) {
                    resp.sendError(HttpServletResponse.SC_CONFLICT, e.getMessage());
                }

            } else if ("update".equals(action)) {
                try {
                    Long userId = Long.parseLong(req.getParameter("userId"));

                    if (!userSession.isAdmin() && !userId.equals(userSession.getCurrentUserId())) {
                        resp.sendError(HttpServletResponse.SC_FORBIDDEN,
                                "Access denied. You can only update your own profile.");
                        return;
                    }

                    String name = req.getParameter("name");
                    String email=req.getParameter("email");
                    Optional<User> updated = userService.updateUser(userId, name,email);

                    if (updated.isPresent()) {
                        resp.sendRedirect(req.getContextPath() + "/users/" + userId);
                    } else {
                        resp.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
                    }
                } catch (NumberFormatException e) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID format");
                }

            } else if ("delete".equals(action)) {
                if (!userSession.isAdmin()) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN,
                            "Access denied. Admin privileges required.");
                    return;
                }
                try {
                    Long userId = Long.parseLong(req.getParameter("userId"));
                    boolean deleted = userService.deleteUser(userId);

                    if (deleted) {
                        resp.sendRedirect(req.getContextPath() + "/users");
                    } else {
                        resp.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
                    }
                } catch (NumberFormatException e) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID format");
                }

            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
            }

        } catch (RuntimeException e) {
            System.err.println("Error processing user request: " + e.getMessage());
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "error occurred while processing your request");
        }
    }
}
