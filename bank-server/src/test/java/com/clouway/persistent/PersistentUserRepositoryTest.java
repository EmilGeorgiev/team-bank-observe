package com.clouway.persistent;

import com.clouway.core.CurrentAmount;
import com.clouway.persistent.util.UserUtil;
import com.clouway.core.User;
import com.google.inject.util.Providers;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import org.junit.Before;
import org.junit.Test;

import java.net.UnknownHostException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class PersistentUserRepositoryTest {

    private PersistentUserRepository persistentUserRepository;
    private DB db;
    private User user;
    
    private UserUtil userUtil;

    @Before
    public void setUp() throws UnknownHostException {
        MongoClient mongoClient = new MongoClient();

        user = new User();

        db = mongoClient.getDB("team-bank-test");

        persistentUserRepository = new PersistentUserRepository(Providers.of(db));

        userUtil = new UserUtil(db);

        users().drop();
    }

    @Test
    public void userIsNotAuthorised() {
        assertThat(persistentUserRepository.isAuthorised(user), is(false));
    }

    @Test
    public void userIsAuthorised() {
        user.setUsername("Brahmaputra");
        user.setPassword("123456");
        assertThat(persistentUserRepository.isAuthorised(user), is(true));
    }

    @Test
    public void getUserName() throws Exception {
        pretendThat(userName("Ivan"), amount(40));

        CurrentAmount currentAmount = persistentUserRepository.getAmountBy("Ivan");

        assertThat(currentAmount.getAmount(), is(40d));

    }

    private double amount(int amount) {
        return amount;
    }

    private void pretendThat(String name, double amount) {
        userUtil.registerClient(name, amount);
    }

    private String userName(String name) {
        return name;
    }

    private DBCollection users() {
        return db.getCollection("users");
    }

}