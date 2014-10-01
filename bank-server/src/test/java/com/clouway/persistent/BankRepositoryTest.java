package com.clouway.persistent;

import com.clouway.core.CurrentUser;
import com.clouway.core.TransactionInfo;
import com.clouway.core.TransactionMessages;
import com.clouway.persistent.util.BankUtil;
import com.google.inject.util.Providers;
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
    private BankUtil bankUtil;
    private DB db;
    private CurrentUser currentUser;

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

        currentUser = new CurrentUser("Ivan");

        persistentBankRepository = new PersistentBankRepository(Providers.of(db),
                Providers.of(currentUser),
                Providers.of(transactionMessages));

        bankUtil = new BankUtil(db);

        bankAccounts().drop();
    }



    @Test
    public void depositAmount() throws Exception {

        pretendThat(clientName("Ivan"), amount(100d));

        TransactionInfo info = persistentBankRepository.deposit(20d);

        assertThat(info.message, is("Success"));
        assertThat(info.amount, is(120d));

    }


    @Test
    public void makeTwoDepositTransactions() throws Exception {

        pretendThat(clientName("Ivan"), amount(100d));

        persistentBankRepository.deposit(20d);
        TransactionInfo info = persistentBankRepository.deposit(80d);

        assertThat(info.message, is("Success"));
        assertThat(info.amount, is(200d));

    }

    @Test
    public void withdrawAmount() throws Exception {

        pretendThat(clientName("Ivan"), amount(200d));

        TransactionInfo info = persistentBankRepository.withdraw(120d);

        assertThat(info.message, is("Success"));
        assertThat(info.amount, is(80d));

    }

    @Test
    public void withdrawMoreThanWeHave() throws Exception {

        pretendThat(clientName("Ivan"), amount(100d));

        TransactionInfo info = persistentBankRepository.withdraw(200d);

        assertThat(info.message, is("Failed"));
        assertThat(info.amount, is(100d));

    }

    private void pretendThat(String clientName, double amount) {
        bankUtil.registerNewUser(clientName, amount);
    }

    private String clientName(String name) {
        return name;
    }

    private double amount(double amount) {
        return amount;
    }

    private DBCollection bankAccounts() {
        return db.getCollection("bank_accounts");
    }
}
