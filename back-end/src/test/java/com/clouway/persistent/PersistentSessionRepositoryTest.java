package com.clouway.persistent;

import com.clouway.core.Clock;
import com.clouway.core.Session;
import com.clouway.core.User;
import com.google.inject.util.Providers;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import org.junit.Before;
import org.junit.Test;

import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.IsNull.notNullValue;

public class PersistentSessionRepositoryTest {

    private PersistentSessionRepository persistentSessionRepository;
    private DB db;
    private Session session;

    @Before
    public void setUp() throws UnknownHostException {

        MongoClient mongoClient = new MongoClient();

        session = new Session("username", "sessionid", new Date());

        User user = new User();
        
        db = mongoClient.getDB("team-bank-test");

        Clock clock = new Clock() {
            @Override
            public Date sessionExpirationTime(Calendar calendar) {

                calendar.set(Calendar.FEBRUARY,3,3,3,0,0);
                return calendar.getTime();
            }

            @Override
            public Date now() {
                return null;
            }
        };

        persistentSessionRepository = new PersistentSessionRepository(Providers.of(db),clock);

        sessions().drop();

        persistentSessionRepository.addUser(user.getName(),session.getSessionId());
    }

    @Test
    public void addUserToSessionRepo() {

        assertThat(db.getCollection("sessions").findOne(), notNullValue());
    }

    @Test
    public void removeUserSession() {

        persistentSessionRepository.remove(session.getSessionId());
        assertThat(db.getCollection("sessions").findOne(), nullValue());
    }

    @Test
    public void getSession() {

        assertThat(persistentSessionRepository.get(session.getSessionId()), is(session));
    }

    private DBCollection sessions() {
        return db.getCollection("sessions");
    }
}