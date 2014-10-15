package com.clouway.persistent;

import com.clouway.core.*;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import java.math.BigDecimal;

/**
 * Created by emil on 14-9-25.
 */
@Singleton
public class PersistentBankRepository implements BankRepository {
    private final Provider<DB> dbProvider;
    private final Provider<CurrentUser> currentUser;
    private final TransactionMessages transactionMessages;

    @Inject
    public PersistentBankRepository(Provider<DB> dbProvider,
                                    Provider<CurrentUser> currentUser,
                                    Provider<TransactionMessages> transactionMessagesProvider) {

        this.dbProvider = dbProvider;
        this.currentUser = currentUser;
        this.transactionMessages = transactionMessagesProvider.get();
    }

    /**
     * Deposit amount in client account.
     * @param amount amount who add in account
     * @return info for transaction and new current amount on the client
     */
    @Override
    public TransactionStatus deposit(BigDecimal amount) {

        DBObject query = new BasicDBObject("name", currentUser.get().name);

        DBObject update = new BasicDBObject("$inc", new BasicDBObject("amount", amount));

        DBObject fields = new BasicDBObject("amount", 1);

        BasicDBObject dbObject = (BasicDBObject) bankAccounts().findAndModify(query, fields, null, false, update, true, false);

        return new TransactionStatus(transactionMessages.onSuccess(), dbObject.getDouble("amount"));
    }

    /**
     * Withdraw amount from client account.If amount who withdraw is greater than current amount
     * transaction is onFailure.
     * @param amount amount who withdraw from account
     * @return info object for transaction and new current amount on the client.
     */
    @Override
    public TransactionStatus withdraw(BigDecimal amount) {

        Double currentAmount = getAmount();

        if (currentAmount < amount.doubleValue()) {
            return new TransactionStatus(transactionMessages.onFailure(), currentAmount);
        }

        DBObject query = new BasicDBObject("name", currentUser.get().name);

        DBObject update = new BasicDBObject("$inc", new BasicDBObject("amount", amount.negate()));

        DBObject fields = new BasicDBObject("amount", 1);

        BasicDBObject dbObject = (BasicDBObject) bankAccounts().findAndModify(query, fields, null, false, update, true, false);

        return new TransactionStatus(transactionMessages.onSuccess(), dbObject.getDouble("amount"));

    }

    @Override
    public double getAmount() {

        DBObject criteria = new BasicDBObject("name", currentUser.get().name);

        DBObject projection = new BasicDBObject("amount", 1)
                .append("_id", 0);

        BasicDBObject dbObject = (BasicDBObject) bankAccounts().findOne(criteria, projection);

        return dbObject.getDouble("amount");

    }

    private DBCollection bankAccounts() {
        return dbProvider.get().getCollection("bank_accounts");
    }


}