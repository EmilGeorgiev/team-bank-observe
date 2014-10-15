package com.clouway.persistent.util;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

/**
 * Created by emil on 14-9-26.
 */
public class UserUtil {


    private final DB db;

    public UserUtil(DB db) {

        this.db = db;
    }

    public void registerClient(String name, double amount) {
        DBObject query = new BasicDBObject("name", name)
                .append("amount", amount);

        users().insert(query);
    }

    private DBCollection users() {
        return db.getCollection("users");
    }
}
