package com.clouway.core;

import java.math.BigDecimal;

/**
 * Created by emil on 14-9-25.
 */
public class TransactionStatus {
    public final String message;
    public final BigDecimal amount;

    public TransactionStatus(String message, BigDecimal amount) {

        this.message = message;
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransactionStatus that = (TransactionStatus) o;

        if (amount != null ? !amount.equals(that.amount) : that.amount != null) return false;
        if (message != null ? !message.equals(that.message) : that.message != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = message != null ? message.hashCode() : 0;
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        return result;
    }
}
