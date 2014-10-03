package com.clouway.core;

/**
 * Created by emil on 14-9-25.
 */
public class Amount {
    private double amount;

    public Amount(double amount) {

        this.amount = amount;
    }

    public Amount() {
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Amount amount1 = (Amount) o;

        if (Double.compare(amount1.amount, amount) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        long temp = Double.doubleToLongBits(amount);
        return (int) (temp ^ (temp >>> 32));
    }
}