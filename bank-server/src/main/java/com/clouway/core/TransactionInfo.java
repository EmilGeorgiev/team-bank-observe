package com.clouway.core;

/**
 * Created by emil on 14-9-25.
 */
public class TransactionInfo {
    public final String message;
    public final double amount;

    public TransactionInfo(String message, double amount) {

        this.message = message;
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransactionInfo that = (TransactionInfo) o;

        if (Double.compare(that.amount, amount) != 0) return false;
        if (message != null ? !message.equals(that.message) : that.message != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = message != null ? message.hashCode() : 0;
        temp = Double.doubleToLongBits(amount);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
