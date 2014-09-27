package com.clouway.persistent.util;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

/**
 * Created by emil on 14-9-25.
 */
public class BankUtil {
    private final DB db;

    public BankUtil(DB db) {

        this.db = db;
    }

    /**
     * Insert new client in database with some amount
     * @param clientName name of the ne client
     * @param amount amount of the new client
     */
    public void deposit(String clientName, double amount) {
        DBObject query = new BasicDBObject("name", clientName)
                .append("amount", amount);

        users().insert(query);
    }

    private DBCollection users() {
        return db.getCollection("users");
    }
}
