package com.clouway.core;

/**
 * Created by emil on 14-9-25.
 */
public class Client {
    private double amount;

    public Client(String clientName, double amount) {

        this.amount = amount;
    }

    public Client(double v) {
    }

    public double getAmount() {
        return amount;
    }
}
