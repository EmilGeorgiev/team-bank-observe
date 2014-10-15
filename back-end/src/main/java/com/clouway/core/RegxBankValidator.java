package com.clouway.core;

/**
 * Created by clouway on 14-10-3.
 */
public class RegxBankValidator implements BankValidator{
    @Override
    public boolean transactionIsValid(Double amount) {
        return String.valueOf(amount).matches("^[1-9][0-9]*(\\.[0-9]{1,2})?$");
    }
}