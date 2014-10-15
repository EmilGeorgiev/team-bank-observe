package com.clouway.persistent;

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
import static org.hamcrest.core.IsNull.notNullValue;

public class PersistentUserRepositoryTest {

    private PersistentUserRepository persistentUserRepository;
    private DB db;
    private User user;

    @Before
    public void setUp() throws UnknownHostException {
        MongoClient mongoClient = new MongoClient();

        user = new User();

        db = mongoClient.getDB("team-bank-test");

        persistentUserRepository = new PersistentUserRepository(Providers.of(db));

        users().drop();
        accounts().drop();
    }

    @Test
    public void userBankAccountWasCreatedAfterRegistration() {
        user.setName("Ivan");
        user.setPassword("123456");
        persistentUserRepository.add(user);
        assertThat(accounts().findOne(), notNullValue());
    }

    private DBCollection users() {
        return db.getCollection("users");
    }

    private DBCollection accounts(){
        return db.getCollection("bank_accounts");
    }

}