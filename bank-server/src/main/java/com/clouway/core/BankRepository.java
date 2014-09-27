package com.clouway.core;

/**
 * Created by emil on 14-9-25.
 */
public interface BankRepository {
    TransactionInfo deposit(String clientName, double amount);

    TransactionInfo withdraw(String clientName, double amount);

    double getAmountBy(String clientName);
}
