package com.paymenthandler.web;

import com.paymenthandler.model.PaymentRequest;
import com.paymenthandler.model.PaymentResponse;
import com.paymenthandler.service.PaymentService;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/payment/process")
public class PaymentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Inject
    private PaymentService paymentService;

    @Inject
    private UserSession userSession;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        userSession.incrementPageViews();
        req.getRequestDispatcher("/views/payment-form.jsp").forward(req, resp);
    }

     
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
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
