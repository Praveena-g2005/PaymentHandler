package com.paymenthandler.config;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

import com.paymenthandler.dao.BalanceDao;
import com.paymenthandler.dao.TransactionDao;
import com.paymenthandler.dao.UserDao;
import com.paymenthandler.payment.PaymentHandler;
import com.paymenthandler.payment.WalletPaymentHandler;
import com.paymenthandler.persistence.DatabaseConnectionFactory;
import com.paymenthandler.persistence.JooqBalanceDao;
import com.paymenthandler.persistence.JooqTransactionDao;
import com.paymenthandler.persistence.JooqUserDao;
import com.paymenthandler.service.AuthenticationService;
import com.paymenthandler.service.BalanceService;
import com.paymenthandler.service.PasswordService;
import com.paymenthandler.service.PaymentService;
import com.paymenthandler.service.UserService;

import javax.sql.DataSource;

public class PaymentHandlerModule extends AbstractModule {

    @Override
    protected void configure() {
       
        bind(DatabaseConnectionFactory.class).in(Singleton.class);
        bind(UserDao.class)
            .annotatedWith(Names.named("jooqUserDao"))
            .to(JooqUserDao.class)
            .in(Singleton.class);

        bind(BalanceDao.class)
            .annotatedWith(Names.named("balanceDao"))
            .to(JooqBalanceDao.class)
            .in(Singleton.class);

        bind(TransactionDao.class)
            .to(JooqTransactionDao.class)
            .in(Singleton.class);

        // Services
        bind(UserService.class).in(Singleton.class);
        bind(BalanceService.class).in(Singleton.class);
        bind(PaymentService.class).in(Singleton.class);
        bind(AuthenticationService.class).in(Singleton.class);
        bind(PasswordService.class).in(Singleton.class);

        
        Multibinder<PaymentHandler> handlerBinder =
            Multibinder.newSetBinder(binder(), PaymentHandler.class);
        handlerBinder.addBinding().to(WalletPaymentHandler.class);
    }
    @Provides
    @Singleton
    @Named("dataSource")
    DataSource provideDataSource(DatabaseConnectionFactory factory) {
        return factory.getDataSource();
    }
}
