package com.paymenthandler.web;

import com.google.inject.Provider;
import com.paymenthandler.model.DepositRequest;
import com.paymenthandler.model.DepositResponse;
import com.paymenthandler.service.DepositService;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class WalletServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final DepositService depositService;
    private final Provider<UserSession> userSessionProvider;

    @Inject
    public WalletServlet(DepositService depositService, Provider<UserSession> userSessionProvider) {
        this.depositService = depositService;
        this.userSessionProvider = userSessionProvider;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/views/wallet-deposit.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            UserSession userSession = userSessionProvider.get();
            Long userId = userSession.getCurrentUserId();

            double amount = Double.parseDouble(req.getParameter("amount"));
            String cardNumber = req.getParameter("cardNumber");
            int expiryMonth = Integer.parseInt(req.getParameter("expiryMonth"));
            int expiryYear = Integer.parseInt(req.getParameter("expiryYear"));
            String cvv = req.getParameter("cvv");
            String cardholderName = req.getParameter("cardholderName");

            DepositRequest depositRequest = new DepositRequest(
                userId, amount, cardNumber, expiryMonth, expiryYear, cvv, cardholderName
            );

            DepositResponse response = depositService.deposit(depositRequest);

            req.setAttribute("response", response);
            req.getRequestDispatcher("/views/deposit-result.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid input parameters: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Validation error: " + e.getMessage());
        } catch (RuntimeException e) {
            System.err.println("Error processing deposit: " + e.getMessage());
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing deposit");
        }
    }
}
