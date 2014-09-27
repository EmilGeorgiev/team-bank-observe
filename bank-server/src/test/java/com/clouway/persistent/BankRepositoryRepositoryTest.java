package com.clouway.persistent;

import com.clouway.core.TransactionInfo;
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
public class BankRepositoryRepositoryTest {

    private PersistentBankRepository persistentBankRepository;
    private BankUtil bankUtil;
    private DB db;


    @Before
    public void setUp() throws UnknownHostException {
        MongoClient mongoClient = new MongoClient();

        db = mongoClient.getDB("TeamBankTest");

        persistentBankRepository = new PersistentBankRepository(Providers.of(db));

        bankUtil = new BankUtil(db);

        clients().drop();
    }



    @Test
    public void depositAmount() throws Exception {

        pretendThat(clientName("Ivan"), amount(100d));

        TransactionInfo info = persistentBankRepository.deposit("Ivan", 20d);

        assertThat(info.message, is("Success"));
        assertThat(info.amount, is(120d));






    }

//    private Matcher<String> matcher(final Object expected) {
//
//        return new BaseMatcher<String>() {
//
//            protected Object theExpected = expected;
//
//            @Override
//            public boolean matches(Object o) {
//                final String str = (String) o;
//                boolean bool = theExpected.equals(str);
//                return bool;
//            }
//
//            @Override
//            public void describeTo(Description description) {
//                description.appendText(theExpected.toString());
//                }
//        };
//    }

    @Test
    public void makeTwoDepositTransaction() throws Exception {

        pretendThat(clientName("Emil"), amount(100d));

        persistentBankRepository.deposit("Emil", 20d);
        TransactionInfo info = persistentBankRepository.deposit("Emil", 80d);

        assertThat(info.message, is("Success"));
        assertThat(info.amount, is(200d));

    }

    @Test
    public void withdrawAmount() throws Exception {

        pretendThat(clientName("Test"), amount(200d));

        TransactionInfo info = persistentBankRepository.withdraw("Test", 120d);

        assertThat(info.message, is("Success"));
        assertThat(info.amount, is(80d));

    }

    @Test
    public void withdrawMoreThanWeHave() throws Exception {

        pretendThat(clientName("Petar"), amount(100d));

        TransactionInfo info = persistentBankRepository.withdraw("Petar", 200d);

        assertThat(info.message, is("Error"));
        assertThat(info.amount, is(100d));

    }

    private void pretendThat(String clientName, double amount) {
        bankUtil.deposit(clientName, amount);
    }

    private String clientName(String name) {
        return name;
    }

    private double amount(double amount) {
        return amount;
    }

    private DBCollection clients() {
        return db.getCollection("clients");
    }
}
