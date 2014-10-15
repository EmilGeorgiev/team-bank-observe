package com.clouway.persistent;

import com.clouway.core.CurrentUser;
import com.clouway.core.TransactionInfo;
import com.clouway.core.TransactionMessages;
import com.google.inject.util.Providers;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import org.junit.Before;
import org.junit.Test;

import java.net.UnknownHostException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by emil on 14-9-25.
 */
public class BankRepositoryTest {

    private PersistentBankRepository persistentBankRepository;
    private DB db;

    private TransactionMessages transactionMessages = new TransactionMessages() {
        @Override
        public String success() {
            return "Success";
        }

        @Override
        public String failed() {
            return "Failed";
        }
    };


    @Before
    public void setUp() throws UnknownHostException {
        MongoClient mongoClient = new MongoClient();

        db = mongoClient.getDB("team-bank-test");

        CurrentUser currentUser = new CurrentUser("Ivan");

        persistentBankRepository = new PersistentBankRepository(Providers.of(db),
                Providers.of(currentUser),
                Providers.of(transactionMessages));

        bankAccounts().drop();

        bankAccounts().insert(new BasicDBObject("name", "Ivan").append("amount", 100d));
    }



    @Test
    public void depositAmount() throws Exception {

        TransactionInfo info = persistentBankRepository.deposit(20d);

        assertThat(info.message, is("Success"));
        assertThat(info.amount, is(120d));

    }


    @Test
    public void makeTwoDepositTransactions() throws Exception {

        persistentBankRepository.deposit(20d);

        TransactionInfo info = persistentBankRepository.deposit(80d);

        assertThat(info.message, is("Success"));
        assertThat(info.amount, is(200d));

    }

    @Test
    public void withdrawAmount() throws Exception {

        TransactionInfo info = persistentBankRepository.withdraw(20d);

        assertThat(info.message, is("Success"));
        assertThat(info.amount, is(80d));

    }

    @Test
    public void withdrawMoreThanWeHave() throws Exception {

        TransactionInfo info = persistentBankRepository.withdraw(200d);

        assertThat(info.message, is("Failed"));
        assertThat(info.amount, is(100d));

    }

    private DBCollection bankAccounts() {
        return db.getCollection("bank_accounts");
    }
}
