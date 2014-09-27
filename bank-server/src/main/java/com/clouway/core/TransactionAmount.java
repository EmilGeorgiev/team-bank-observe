package com.clouway.core;

/**
 * Created by emil on 14-9-25.
 */
public class TransactionAmount {
    private double amount;

    public TransactionAmount(String clientName, double amount) {

        this.amount = amount;
    }

    public TransactionAmount(double v) {
    }

    public double getAmount() {
        return amount;
    }
}
