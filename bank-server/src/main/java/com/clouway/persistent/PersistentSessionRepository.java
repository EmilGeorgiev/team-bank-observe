package com.clouway.persistent;

import com.clouway.core.CalendarUtil;
import com.clouway.core.SessionRepository;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;

/**
 * Created by emil on 14-9-27.
 */
public class PersistentSessionRepository implements SessionRepository {

    private DB db;

    @Inject
    public PersistentSessionRepository(Provider<DB> dbProvider) {

        this.db = dbProvider.get();
    }

    @Override
    public void addUser(String username, String sessionId) {
        
        BasicDBObject query = new BasicDBObject();

        query.append("username", username);
        query.append("sessionId", sessionId);
        query.append("expirationTime", CalendarUtil.sessionExpirationTime());

        sessions().createIndex(new BasicDBObject("expirationTime", 1), new BasicDBObject("expireAfterSeconds", 0));

        sessions().insert(query);
    }

    @Override
    public void remove(String sessionId) {

        BasicDBObject query = new BasicDBObject();

        query.append("sessionId",sessionId);

        sessions().remove(query);
    }

    private DBCollection sessions() {
        return db.getCollection("sessions");
    }
}
