package com.paymenthandler.factory;

import java.util.*;
import javax.enterprise.inject.Produces;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UtilsProducers{
    
    @Produces
    public String produceUUID(){
        return UUID.randomUUID().toString();
    }
}