package com.paymenthandler.dao;

import com.paymenthandler.model.FeeConfiguration;

import java.util.List;
import java.util.Optional;

public interface FeeConfigurationDao {

    FeeConfiguration save(FeeConfiguration config);

    FeeConfiguration update(FeeConfiguration config);

    Optional<FeeConfiguration> findByPaymentMethod(String paymentMethod);

    List<FeeConfiguration> findAll();

    boolean deleteByPaymentMethod(String paymentMethod);
}
