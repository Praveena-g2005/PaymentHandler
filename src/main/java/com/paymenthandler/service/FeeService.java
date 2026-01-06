package com.paymenthandler.service;

import com.paymenthandler.dao.FeeConfigurationDao;
import com.paymenthandler.enums.FeeType;
import com.paymenthandler.enums.PaymentMethod;
import com.paymenthandler.model.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Singleton
public class FeeService {

    private static final Logger LOGGER = Logger.getLogger(FeeService.class.getName());
    private static final double MINIMUM_FEE = 0.10;

    private final FeeConfigurationDao feeConfigurationDao;

    @Inject
    public FeeService(FeeConfigurationDao feeConfigurationDao) {
        this.feeConfigurationDao = feeConfigurationDao;
    }

    public FeeCalculationResult calculateFee(double baseAmount, String paymentMethod) {
        
        PaymentMethod method = PaymentMethod.fromString(paymentMethod);
        Optional<FeeConfiguration> feeConfig = feeConfigurationDao.findByPaymentMethod(
            method.getValue()
        );

        double feeAmount;

        if (feeConfig.isPresent()) {
            feeAmount = feeConfig.get().calculateFee(baseAmount);
        } else {
            LOGGER.warning(String.format(
                "Fee configuration not found in database for payment method '%s'. " +
                "Using default fee percentage %.4f%%.",
                method.getValue(),
                method.getDefaultFeePercentage() * 100
            ));
            feeAmount = baseAmount * method.getDefaultFeePercentage();
        }

        feeAmount = Math.max(feeAmount, MINIMUM_FEE);
        feeAmount = Math.ceil(feeAmount * 100) / 100.0;  

        return new FeeCalculationResult(baseAmount, feeAmount);
    }

    public FeeConfiguration saveFeeConfiguration(FeeConfigurationRequest request) {
        
        PaymentMethod.fromString(request.getPaymentMethod());
        FeeConfiguration config = FeeConfiguration.builder()
            .paymentMethod(request.getPaymentMethod())
            .feeType(FeeType.fromString(request.getFeeType()))
            .feeValue(request.getFeeValue())
            .build();
        return feeConfigurationDao.save(config);
    }

    public FeeConfiguration updateFeeConfiguration(FeeConfigurationRequest request) {
        
        PaymentMethod.fromString(request.getPaymentMethod());
        Optional<FeeConfiguration> existing = feeConfigurationDao.findByPaymentMethod(
            request.getPaymentMethod()
        );

        if (!existing.isPresent()) {
            throw new IllegalArgumentException(
                "Fee configuration not found for payment method: " + request.getPaymentMethod()
            );
        }

        FeeConfiguration config = FeeConfiguration.builder()
            .id(existing.get().getId())
            .paymentMethod(request.getPaymentMethod())
            .feeType(FeeType.fromString(request.getFeeType()))
            .feeValue(request.getFeeValue())
            .build();

        return feeConfigurationDao.update(config);
    }

    public Optional<FeeConfiguration> getFeeConfiguration(String paymentMethod) {
        PaymentMethod method = PaymentMethod.fromString(paymentMethod);
        return feeConfigurationDao.findByPaymentMethod(method.getValue());
    }

    public List<FeeConfiguration> getAllFeeConfigurations() {
        return feeConfigurationDao.findAll();
    }

    public boolean deleteFeeConfiguration(String paymentMethod) {
        PaymentMethod method = PaymentMethod.fromString(paymentMethod);

        boolean deleted = feeConfigurationDao.deleteByPaymentMethod(method.getValue());

        if (deleted) {
            LOGGER.warning(String.format(
                "Fee configuration deleted for payment method '%s'. ",
                method.getValue(),
                method.getDefaultFeePercentage() * 100
            ));
        }

        return deleted;
    }
}
