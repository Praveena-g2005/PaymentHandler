package com.paymenthandler.config;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

import com.paymenthandler.dao.BalanceDao;
import com.paymenthandler.dao.FeeConfigurationDao;
import com.paymenthandler.dao.TransactionDao;
import com.paymenthandler.dao.UserDao;
import com.paymenthandler.dao.impl.BalanceDaoImpl;
import com.paymenthandler.dao.impl.FeeConfigurationDaoImpl;
import com.paymenthandler.dao.impl.TransactionDaoImpl;
import com.paymenthandler.dao.impl.UserDaoImpl;
import com.paymenthandler.payment.PaymentHandler;
import com.paymenthandler.payment.WalletPaymentHandler;
import com.paymenthandler.payment.gateway.MockPaymentGateway;
import com.paymenthandler.payment.gateway.PaymentGateway;
import com.paymenthandler.persistence.DatabaseConnectionFactory;
import com.paymenthandler.service.AuthenticationService;
import com.paymenthandler.service.BalanceService;
import com.paymenthandler.service.DepositService;
import com.paymenthandler.service.FeeService;
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
            .to(UserDaoImpl.class)
            .in(Singleton.class);

        bind(BalanceDao.class)
            .annotatedWith(Names.named("balanceDao"))
            .to(BalanceDaoImpl.class)
            .in(Singleton.class);

        bind(TransactionDao.class)
            .to(TransactionDaoImpl.class)
            .in(Singleton.class);

        bind(FeeConfigurationDao.class)
            .to(FeeConfigurationDaoImpl.class)
            .in(Singleton.class);

        bind(PaymentGateway.class)
            .to(MockPaymentGateway.class)
            .in(Singleton.class);

        // Services
        bind(UserService.class).in(Singleton.class);
        bind(BalanceService.class).in(Singleton.class);
        bind(FeeService.class).in(Singleton.class);
        bind(DepositService.class).in(Singleton.class);
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
