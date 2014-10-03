package com.clouway.core;

/**
 * Created by clouway on 14-10-3.
 */
public class RegxBankValidator implements BankValidator{
    @Override
    public boolean transactionIsValid(Amount amount) {
        return String.valueOf(amount.getAmount()).matches("^[1-9][0-9]*(\\.[0-9]{1,2})?$");
    }
}