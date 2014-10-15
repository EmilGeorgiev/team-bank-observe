package com.clouway.persistent;

import com.clouway.core.CurrentUser;
import com.clouway.core.TransactionStatus;
import com.clouway.core.TransactionMessages;
import com.google.inject.util.Providers;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.net.UnknownHostException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by emil on 14-9-25.
 */
public class BankRepositoryTest {

    private PersistentBankRepository persistentBankRepository;
    private DB db;
    private BigDecimal decimal = new BigDecimal(20);

    private TransactionMessages transactionMessages = new TransactionMessages() {
        @Override
        public String onSuccess() {
            return "Success";
        }

        @Override
        public String onFailure() {
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

        bankAccounts().insert(new BasicDBObject("name", "Ivan").append("amount", new BigDecimal(100)));
    }



    @Test
    public void depositAmount() throws Exception {

        TransactionStatus info = persistentBankRepository.deposit(decimal);

        assertThat(info.message, is("Success"));
        assertThat(info.amount, is(120d));

    }


    @Test
    public void makeTwoDepositTransactions() throws Exception {


        persistentBankRepository.deposit(decimal);

        TransactionStatus info = persistentBankRepository.deposit(new BigDecimal(80));

        assertThat(info.message, is("Success"));
        assertThat(info.amount, is(200d));

    }

    @Test
    public void withdrawAmount() throws Exception {

        TransactionStatus status = persistentBankRepository.withdraw(decimal);

        assertThat(status.message, is("Success"));
        assertThat(status.amount, is(80d));

    }

    @Test
    public void withdrawMoreThanWeHave() throws Exception {

        TransactionStatus status = persistentBankRepository.withdraw(new BigDecimal(200));

        assertThat(status.message, is("Failed"));
        assertThat(status.amount, is(100d));

    }

    private DBCollection bankAccounts() {
        return db.getCollection("bank_accounts");
    }
}
