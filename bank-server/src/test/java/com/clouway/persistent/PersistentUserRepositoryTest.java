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

public class PersistentUserRepositoryTest {

    private PersistentUserRepository persistentUserRepository;
    private DB db;
    private User user;
    

    @Before
    public void setUp() throws UnknownHostException {
        MongoClient mongoClient = new MongoClient();

        db = mongoClient.getDB("team-bank-test");

        persistentUserRepository = new PersistentUserRepository(Providers.of(db));

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

    private DBCollection users() {
        return db.getCollection("users");
    }

}