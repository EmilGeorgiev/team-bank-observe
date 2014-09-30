package com.clouway.persistent;

import com.google.inject.util.Providers;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import org.junit.Before;

import java.net.UnknownHostException;

public class PersistentSessionRepositoryTest {

    private PersistentSessionRepository persistentSessionRepository;
    private DB db;

    @Before
    public void setUp() throws UnknownHostException {

        MongoClient mongoClient = new MongoClient();

        db = mongoClient.getDB("team-bank-test");

        persistentSessionRepository = new PersistentSessionRepository(Providers.of(db));

        sessions().drop();

    }

    private DBCollection sessions() {
        return db.getCollection("sessions");
    }

}