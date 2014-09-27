package com.clouway.http.fake;

import com.clouway.core.TransactionAmount;

import static com.google.sitebricks.headless.Request.*;

/**
 * Created by emil on 14-9-25.
 */
public class FakeRequestReader implements RequestRead {
    public final String clientName;
    public final double amount;

    public FakeRequestReader(String clientName, double amount) {

        this.clientName = clientName;
        this.amount = amount;
    }

    @Override
    public Object as(Class aClass) {
        return new TransactionAmount(amount);
    }
}
