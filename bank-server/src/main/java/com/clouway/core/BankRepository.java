package com.clouway.core;

/**
 * Created by emil on 14-9-25.
 */
public interface BankRepository {
    TransactionInfo deposit(double amount);

    TransactionInfo withdraw(double amount);

    Amount getAmountBy();
}
