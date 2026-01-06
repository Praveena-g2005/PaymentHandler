package com.paymenthandler.config;

import com.google.inject.servlet.ServletModule;
import com.paymenthandler.web.AuthenticationFilter;
import com.paymenthandler.web.AuthenticationServlet;
import com.paymenthandler.web.FeeConfigurationServlet;
import com.paymenthandler.web.PaymentServlet;
import com.paymenthandler.web.UserServlet;
import com.paymenthandler.web.UserSession;
import com.paymenthandler.web.UserSessionProvider;
import com.paymenthandler.web.WalletServlet;

public class PaymentHandlerServletModule extends ServletModule {

    @Override
    protected void configureServlets() {
        
        bind(UserSession.class).toProvider(UserSessionProvider.class);

        filter("/*").through(AuthenticationFilter.class);

        // Servlets
        serve("/payment/process").with(PaymentServlet.class);
        serve("/auth/login", "/auth/register", "/auth/logout").with(AuthenticationServlet.class);
        serve("/users", "/users/*").with(UserServlet.class);
        serve("/fee-config").with(FeeConfigurationServlet.class);
        serve("/wallet/deposit").with(WalletServlet.class);
    }
}
