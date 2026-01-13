package com.paymenthandler.web;

import com.google.inject.Provider;
import com.paymenthandler.dto.request.PaymentRequest;
import com.paymenthandler.dto.response.PaymentResponse;
import com.paymenthandler.service.PaymentService;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class PaymentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final PaymentService paymentService;
    private final Provider<UserSession> userSessionProvider;
    private final com.paymenthandler.service.FeeService feeService;

    @Inject
    public PaymentServlet(PaymentService paymentService, Provider<UserSession> userSessionProvider, com.paymenthandler.service.FeeService feeService) {
        this.paymentService = paymentService;
        this.userSessionProvider = userSessionProvider;
        this.feeService = feeService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        UserSession userSession = userSessionProvider.get();
        userSession.incrementPageViews();

        double cardFeePercentage = getFeePercentage("card");
        double upiFeePercentage = getFeePercentage("upi");
        double walletFeePercentage = getFeePercentage("wallet");

        req.setAttribute("cardFeePercentage", cardFeePercentage);
        req.setAttribute("upiFeePercentage", upiFeePercentage);
        req.setAttribute("walletFeePercentage", walletFeePercentage);

        req.getRequestDispatcher("/views/payment-form.jsp").forward(req, resp);
    }

    private double getFeePercentage(String paymentMethod) {
        java.util.Optional<com.paymenthandler.model.FeeConfiguration> feeConfig =
            feeService.getFeeConfiguration(paymentMethod);

        if (feeConfig.isPresent()) {
            com.paymenthandler.model.FeeConfiguration config = feeConfig.get();
            if (config.getFeeType() == com.paymenthandler.enums.FeeType.PERCENTAGE) {
                return config.getFeeValue();
            }
        }

        com.paymenthandler.enums.PaymentMethod method =
            com.paymenthandler.enums.PaymentMethod.fromString(paymentMethod);
        return method.getDefaultFeePercentage() * 100;
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            UserSession userSession = userSessionProvider.get();
            Long payerId = Long.parseLong(req.getParameter("payerId"));

            if (!userSession.isAdmin() && !payerId.equals(userSession.getCurrentUserId())) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "Access denied. You can only process payments for your own account.");
                return;
            }

            String payeeIdStr = req.getParameter("payeeId");
            Long payeeId = (payeeIdStr != null && !payeeIdStr.trim().isEmpty())
                ? Long.parseLong(payeeIdStr)
                : null;
            Double amount = Double.parseDouble(req.getParameter("amount"));
            String method = req.getParameter("method");

            PaymentRequest paymentRequest = new PaymentRequest(
                payerId, payeeId, amount, method
            );

            PaymentResponse response = paymentService.process(paymentRequest);

            req.setAttribute("paymentRequest", paymentRequest);
            req.setAttribute("response", response);
            
            if (response.getFeeAmount() != null) {
                req.setAttribute("feeAmount", String.format("%.2f", response.getFeeAmount()));
            }
            if (response.getTotalAmount() != null) {
                req.setAttribute("totalAmount", String.format("%.2f", response.getTotalAmount()));
            }
            req.getRequestDispatcher("/views/payment-result.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                "Invalid payment parameters: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                "Validation error: " + e.getMessage());
        } catch (RuntimeException e) {
            System.err.println("Error processing payment: " + e.getMessage());
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                "An error occurred while processing payment");
        }
    }
}
