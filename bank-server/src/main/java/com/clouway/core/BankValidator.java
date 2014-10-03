package com.clouway.core;

/**
 * Created by clouway on 14-10-3.
 */
public interface BankValidator {

    boolean transactionIsValid(Amount amount);
}