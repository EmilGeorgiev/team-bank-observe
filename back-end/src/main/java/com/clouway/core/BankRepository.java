package com.clouway.core;

import java.math.BigDecimal;

/**
 * Created by emil on 14-9-25.
 */
public interface BankRepository {
    TransactionStatus deposit(BigDecimal amount);

    TransactionStatus withdraw(BigDecimal amount);

    double getAmount();
}
