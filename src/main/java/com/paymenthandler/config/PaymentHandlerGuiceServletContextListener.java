package com.paymenthandler.config;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

public class PaymentHandlerGuiceServletContextListener extends GuiceServletContextListener {
    @Override
    protected Injector getInjector() {
        return Guice.createInjector(
            new PaymentHandlerModule(),
            new PaymentHandlerServletModule()
        );
    }
}
