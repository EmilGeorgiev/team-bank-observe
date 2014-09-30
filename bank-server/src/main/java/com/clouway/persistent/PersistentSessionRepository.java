package com.clouway.persistent;

import com.clouway.core.Clock;
import com.clouway.core.Session;
import com.clouway.core.SessionRepository;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by emil on 14-9-27.
 */
public class PersistentSessionRepository implements SessionRepository {

    private DB db;
    private Clock clock;

    @Inject
    public PersistentSessionRepository(Provider<DB> dbProvider, Clock clock) {

        this.clock = clock;
        this.db = dbProvider.get();
    }

    @Override
    public void addUser(String username, String sessionId) {
        
        BasicDBObject query = new BasicDBObject();

        query.append("username", username);
        query.append("sessionId", sessionId);
        query.append("expirationTime", clock.sessionExpirationTime(Calendar.getInstance()));

        sessions().createIndex(new BasicDBObject("expirationTime", 1), new BasicDBObject("expireAfterSeconds", 0));

        sessions().insert(query);
    }

    @Override
    public void remove(String sessionId) {

        BasicDBObject query = new BasicDBObject();

        query.append("sessionId",sessionId);

        sessions().remove(query);
    }

    @Override
    public Session get(String sessionId) {

        BasicDBObject query = new BasicDBObject("sessionId",sessionId);

        BasicDBObject fields = new BasicDBObject();

        fields.put("username", 1);
        fields.put("sessionId", 2);
        fields.put("expirationTime",3);

        DBObject session = sessions().findOne(query,fields);

        String username = (String) session.get("username");
        String id = (String) session.get("sessionId");
        Date expirationTime = (Date)session.get("expirationTime");

        return new Session(username,id,expirationTime);
    }

    private DBCollection sessions() {
        return db.getCollection("sessions");
    }
}