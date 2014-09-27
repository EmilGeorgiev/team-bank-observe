package com.clouway.persistent;

import com.google.inject.util.Providers;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import org.junit.Before;

import java.net.UnknownHostException;

public class PersistentUserRepositoryTest {

    private PersistentUserRepository persistentUserRepository;
    private DB db;

    @Before
    public void setUp() throws UnknownHostException {
        MongoClient mongoClient = new MongoClient();

        db = mongoClient.getDB("team-bank-test");

        persistentUserRepository = new PersistentUserRepository(Providers.of(db));

        users().drop();
    }

    private DBCollection users() {
        return db.getCollection("users");
    }

}