package com.clouway.persistent;

import com.clouway.core.*;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

/**
 * Created by emil on 14-9-25.
 */
public class PersistentBankRepository implements BankRepository {
    private final Provider<DB> dbProvider;
    private CurrentUser currentUser;
    private TransactionMessages transactionMessages;

    @Inject
    public PersistentBankRepository(Provider<DB> dbProvider,
                                    Provider<CurrentUser> currentUserProvider,
                                    Provider<TransactionMessages> transactionMessagesProvider) {

        this.dbProvider = dbProvider;
        this.currentUser = currentUserProvider.get();
        this.transactionMessages = transactionMessagesProvider.get();
    }

    /**
     * Deposit amount in client account.
     * @param amount amount who add in account
     * @return info for transaction and new current amount on the client
     */
    @Override
    public TransactionInfo deposit(double amount) {

        DBObject query = new BasicDBObject("name", currentUser.getName());

        DBObject update = new BasicDBObject("$inc", new BasicDBObject("amount", amount));

        bankAccounts().update(query, update);

        return new TransactionInfo(transactionMessages.success(), getAmountBy(currentUser.getName()).getAmount());
    }

    /**
     * Withdraw amount from client account.If amount who withdraw is greater than current amount
     * transaction is failed.
     * @param amount amount who withdraw from account
     * @return info object for transaction and new current amount on the client.
     */
    @Override
    public TransactionInfo withdraw(double amount) {

        Double currentAmount = getCurrentAmount(currentUser.getName());

        if(currentAmount < amount) {
            return new TransactionInfo(transactionMessages.failed(), currentAmount);
        }

        DBObject query = new BasicDBObject("name", currentUser.getName());

        DBObject update = new BasicDBObject("$inc", new BasicDBObject("amount", -amount));

        bankAccounts().update(query, update);

        return new TransactionInfo(transactionMessages.success(), getCurrentAmount(currentUser.getName()));

    }

    @Override
    public Amount getAmountBy(String clientName) {

        DBObject criteria = new BasicDBObject("name", clientName);

        DBObject projection = new BasicDBObject("amount", 1)
                .append("_id", 0);

        BasicDBObject dbObject = (BasicDBObject) bankAccounts().findOne(criteria, projection);

        return  new Amount(dbObject.getDouble("amount"));

    }

    private Double getCurrentAmount(String username) {
        DBObject criteria = new BasicDBObject("name", username);

        DBObject projection = new BasicDBObject("amount", 1)
                .append("_id", 0);

        BasicDBObject dbObject = (BasicDBObject) bankAccounts().findOne(criteria, projection);

        return  dbObject.getDouble("amount");
    }

    private DBCollection bankAccounts() {
        return dbProvider.get().getCollection("bank_accounts");
    }


}
