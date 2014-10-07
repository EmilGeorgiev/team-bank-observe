package com.clouway.persistent;

import com.clouway.core.*;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

/**
 * Created by emil on 14-9-25.
 */
@Singleton
public class PersistentBankRepository implements BankRepository {
    private final Provider<DB> dbProvider;
    private Provider<CurrentUser> currentUser;
    private TransactionMessages transactionMessages;

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
    public TransactionInfo deposit(double amount) {

        DBObject query = new BasicDBObject("name", currentUser.get().getName());

        DBObject update = new BasicDBObject("$inc", new BasicDBObject("amount", amount));

        bankAccounts().update(query, update);

        return new TransactionInfo(transactionMessages.success(), getAmount());
        //TODO: change this shit: getAmount(), getAmount???
    }

    /**
     * Withdraw amount from client account.If amount who withdraw is greater than current amount
     * transaction is failed.
     * @param amount amount who withdraw from account
     * @return info object for transaction and new current amount on the client.
     */
    @Override
    public TransactionInfo withdraw(double amount) {

        Double currentAmount = getAmount();

        if (currentAmount < amount) {
            return new TransactionInfo(transactionMessages.failed(), currentAmount);
        }

        DBObject query = new BasicDBObject("name", currentUser.get().getName());

        DBObject update = new BasicDBObject("$inc", new BasicDBObject("amount", -amount));

        bankAccounts().update(query, update);

        return new TransactionInfo(transactionMessages.success(), getAmount());

    }

    @Override
    public double getAmount() {

        DBObject criteria = new BasicDBObject("name", currentUser.get().getName());

        DBObject projection = new BasicDBObject("amount", 1)
                .append("_id", 0);

        BasicDBObject dbObject = (BasicDBObject) bankAccounts().findOne(criteria, projection);

        return dbObject.getDouble("amount");

    }

    private DBCollection bankAccounts() {
        return dbProvider.get().getCollection("bank_accounts");
    }


}