package com.paymenthandler.web;

import com.google.inject.Provider;
import com.paymenthandler.model.FeeConfiguration;
import com.paymenthandler.model.FeeConfigurationRequest;
import com.paymenthandler.service.FeeService;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

@Singleton
public class FeeConfigurationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final FeeService feeService;
    private final Provider<UserSession> userSessionProvider;

    @Inject
    public FeeConfigurationServlet(FeeService feeService, Provider<UserSession> userSessionProvider) {
        this.feeService = feeService;
        this.userSessionProvider = userSessionProvider;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        UserSession userSession = userSessionProvider.get();

        // Admin-only access
        if (!userSession.isAdmin()) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resp.setContentType("application/json");
            resp.getWriter().write("{\"error\": \"Access denied. Admin privileges required.\"}");
            return;
        }

        try {
            String method = req.getParameter("method");

            if (method != null && !method.trim().isEmpty()) {
                Optional<FeeConfiguration> config = feeService.getFeeConfiguration(method);

                resp.setContentType("application/json");
                PrintWriter out = resp.getWriter();

                if (config.isPresent()) {
                    out.write(toJson(config.get()));
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.write("{\"error\": \"Fee configuration not found for method: " + method + "\"}");
                }
            } else {
                List<FeeConfiguration> configs = feeService.getAllFeeConfigurations();

                resp.setContentType("application/json");
                PrintWriter out = resp.getWriter();

                out.write("[");
                for (int i = 0; i < configs.size(); i++) {
                    out.write(toJson(configs.get(i)));
                    if (i < configs.size() - 1) {
                        out.write(",");
                    }
                }
                out.write("]");
            }

        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("application/json");
            resp.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        } catch (RuntimeException e) {
            System.err.println("Error retrieving fee configuration: " + e.getMessage());
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.setContentType("application/json");
            resp.getWriter().write("{\"error\": \"An error occurred while retrieving fee configuration\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        UserSession userSession = userSessionProvider.get();

        if (!userSession.isAdmin()) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resp.setContentType("application/json");
            resp.getWriter().write("{\"error\": \"Access denied. Admin privileges required.\"}");
            return;
        }

        try {
            String paymentMethod = req.getParameter("paymentMethod");
            String feeType = req.getParameter("feeType");
            String feeValueStr = req.getParameter("feeValue");

            if (paymentMethod == null || feeType == null || feeValueStr == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.setContentType("application/json");
                resp.getWriter().write("{\"error\": \"Missing required parameters: paymentMethod, feeType, feeValue\"}");
                return;
            }

            double feeValue = Double.parseDouble(feeValueStr);

            FeeConfigurationRequest request = new FeeConfigurationRequest(paymentMethod, feeType, feeValue);

            FeeConfiguration saved = feeService.saveFeeConfiguration(request);

            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.setContentType("application/json");
            resp.getWriter().write(toJson(saved));

        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("application/json");
            resp.getWriter().write("{\"error\": \"Invalid feeValue: must be a number\"}");
        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("application/json");
            resp.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        } catch (RuntimeException e) {
            System.err.println("Error creating fee configuration: " + e.getMessage());
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.setContentType("application/json");
            resp.getWriter().write("{\"error\": \"An error occurred while creating fee configuration\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        UserSession userSession = userSessionProvider.get();

        if (!userSession.isAdmin()) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resp.setContentType("application/json");
            resp.getWriter().write("{\"error\": \"Access denied. Admin privileges required.\"}");
            return;
        }

        try {
            String paymentMethod = req.getParameter("paymentMethod");
            String feeType = req.getParameter("feeType");
            String feeValueStr = req.getParameter("feeValue");

            if (paymentMethod == null || feeType == null || feeValueStr == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.setContentType("application/json");
                resp.getWriter().write("{\"error\": \"Missing required parameters: paymentMethod, feeType, feeValue\"}");
                return;
            }

            double feeValue = Double.parseDouble(feeValueStr);

            FeeConfigurationRequest request = new FeeConfigurationRequest(paymentMethod, feeType, feeValue);

            FeeConfiguration updated = feeService.updateFeeConfiguration(request);

            resp.setContentType("application/json");
            resp.getWriter().write(toJson(updated));

        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("application/json");
            resp.getWriter().write("{\"error\": \"Invalid feeValue: must be a number\"}");
        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("application/json");
            resp.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        } catch (RuntimeException e) {
            System.err.println("Error updating fee configuration: " + e.getMessage());
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.setContentType("application/json");
            resp.getWriter().write("{\"error\": \"An error occurred while updating fee configuration\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        UserSession userSession = userSessionProvider.get();

        if (!userSession.isAdmin()) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resp.setContentType("application/json");
            resp.getWriter().write("{\"error\": \"Access denied. Admin privileges required.\"}");
            return;
        }

        try {
            String method = req.getParameter("method");

            if (method == null || method.trim().isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.setContentType("application/json");
                resp.getWriter().write("{\"error\": \"Missing required parameter: method\"}");
                return;
            }

            boolean deleted = feeService.deleteFeeConfiguration(method);

            resp.setContentType("application/json");

            if (deleted) {
                resp.getWriter().write("{\"message\": \"Fee configuration deleted successfully for method: " + method + "\"}");
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("{\"error\": \"Fee configuration not found for method: " + method + "\"}");
            }

        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("application/json");
            resp.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        } catch (RuntimeException e) {
            System.err.println("Error deleting fee configuration: " + e.getMessage());
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.setContentType("application/json");
            resp.getWriter().write("{\"error\": \"An error occurred while deleting fee configuration\"}");
        }
    }

    private String toJson(FeeConfiguration config) {
        return String.format(
            "{\"id\": %d, \"paymentMethod\": \"%s\", \"feeType\": \"%s\", \"feeValue\": %.4f, \"createdAt\": \"%s\", \"updatedAt\": \"%s\"}",
            config.getId(),
            config.getPaymentMethod(),
            config.getFeeType().name(),
            config.getFeeValue(),
            config.getCreatedAt().toString(),
            config.getUpdatedAt().toString()
        );
    }
}
