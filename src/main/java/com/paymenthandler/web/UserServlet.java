package com.paymenthandler.web;

import com.paymenthandler.model.User;
import com.paymenthandler.service.UserService;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet(urlPatterns = {"/users", "/users/*"})
public class UserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Inject
    private UserService userService;

    @Inject
    private UserSession userSession;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

    System.out.println(">>> userSession = " + userSession);
    System.out.println(">>> userService = " + userService);

        userSession.incrementPageViews();

        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            List<User> users = userService.findAllUsers();
            req.setAttribute("users", users);
            req.getRequestDispatcher("/views/users.jsp").forward(req, resp);

        } else {
            
            try {
                Long userId = Long.parseLong(pathInfo.substring(1));
                Optional<User> user = userService.getUserById(userId);

                if (user.isPresent()) {
                    req.setAttribute("user", user.get());
                    req.getRequestDispatcher("/views/user-detail.jsp").forward(req, resp);
                } else {
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
        String action = req.getParameter("action");

        if ("create".equals(action)) {
            String name = req.getParameter("name");
            String email = req.getParameter("email");

            User user = userService.createUser(name, email);

            resp.sendRedirect(req.getContextPath() + "/users/" + user.getId());

        } else if ("update".equals(action)) {
            Long userId = Long.parseLong(req.getParameter("userId"));
            String name = req.getParameter("name");

            Optional<User> updated = userService.updateUserName(userId, name);

            if (updated.isPresent()) {
                resp.sendRedirect(req.getContextPath() + "/users/" + userId);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
            }

        } else if ("delete".equals(action)) {
            Long userId = Long.parseLong(req.getParameter("userId"));
            boolean deleted = userService.deleteUser(userId);

            if (deleted) {
                resp.sendRedirect(req.getContextPath() + "/users");
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
            }

        } else if ("login".equals(action)) {
            Long userId = Long.parseLong(req.getParameter("userId"));
            Optional<User> user = userService.getUserById(userId);

            if (user.isPresent()) {
                userSession.setCurrentUser(user.get());
                resp.sendRedirect(req.getContextPath() + "/");
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
            }

        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
        }
    }
}
