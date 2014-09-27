package com.clouway.persistent;

import com.clouway.core.BankRepository;
import com.clouway.core.TransactionInfo;
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

    @Inject
    public PersistentBankRepository(Provider<DB> dbProvider) {

        this.dbProvider = dbProvider;
    }

    /**
     * Deposit amount in client account.
     * @param clientName name of the client who do transaction
     * @param amount amount who add in account
     * @return info for transaction and new current amount on the client
     */
    @Override
    public TransactionInfo deposit(String clientName, double amount) {

        DBObject query = new BasicDBObject("name", clientName);

        DBObject update = new BasicDBObject("$inc", new BasicDBObject("amount", amount));

        users().update(query, update);

        return new TransactionInfo("Success", getAmountBy(clientName));
    }

    /**
     * Withdraw amount from client account.If amount who withdraw is greater than current amount
     * transaction is failed.
     * @param clientName name of the client who do transaction
     * @param amount amount who withdraw from account
     * @return info object for transaction and new current amount on the client.
     */
    @Override
    public TransactionInfo withdraw(String clientName, double amount) {

        Double currentAmount = getAmountBy(clientName);

        if(currentAmount < amount) {
            return new TransactionInfo("Error", currentAmount);
        }

        DBObject query = new BasicDBObject("name", clientName);

        DBObject update = new BasicDBObject("$inc", new BasicDBObject("amount", -amount));

        users().update(query, update);

        return new TransactionInfo("Success", getAmountBy(clientName));


    }

    @Override
    public double getAmountBy(String clientName) {

        DBObject criteria = new BasicDBObject("name", clientName);

        DBObject projection = new BasicDBObject("amount", 1)
                .append("_id", 0);

        BasicDBObject amount = (BasicDBObject) users().findOne(criteria, projection);

        return amount.getDouble("amount");

    }

    private DBCollection users() {
        return dbProvider.get().getCollection("users");
    }


}
