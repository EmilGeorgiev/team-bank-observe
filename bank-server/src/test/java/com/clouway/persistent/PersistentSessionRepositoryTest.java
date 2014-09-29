package com.clouway.persistent;

import com.clouway.core.Session;
import com.clouway.core.User;
import com.google.inject.util.Providers;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import org.junit.Before;
import org.junit.Test;

import java.net.UnknownHostException;

import static org.hamcrest.MatcherAssert.assertThat;

public class PersistentSessionRepositoryTest {

    private PersistentSessionRepository persistentSessionRepository;
    private DB db;
    private User user;
    private Session session;

    @Before
    public void setUp() throws UnknownHostException {

        MongoClient mongoClient = new MongoClient();

        user = new User();
        
        db = mongoClient.getDB("team-bank-test");

        persistentSessionRepository = new PersistentSessionRepository(Providers.of(db));

        sessions().drop();

    }

    @Test
    public void addUserToSessionRepo() {
        persistentSessionRepository.addUser(user.getUsername(), session.getSessionId());
        assertThat(db.getCollection("sessions").findOne(), notNullValue());
    }

    @Test
    public void removeUserSession() {
        persistentSessionRepository.remove(session.getSessionId());
        assertThat(db.getCollection("sessions").findOne(), nullVa
                lue());
    }

    private DBCollection sessions() {
        return db.getCollection("sessions");
    }

}