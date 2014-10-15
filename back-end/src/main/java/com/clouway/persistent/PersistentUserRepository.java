package com.clouway.persistent;

import com.clouway.core.User;
import com.clouway.core.UserRepository;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

/**
 * Created by emil on 14-9-27.
 */
@Singleton
public class PersistentUserRepository implements UserRepository {


    private final DB db;

    @Inject
    public PersistentUserRepository(Provider<DB> dbProvider) {

        this.db = dbProvider.get();
    }


    @Override
    public void add(User user) {

        BasicDBObject query = new BasicDBObject();

        query.append("username",user.getName());
        query.append("password",user.getPassword());

        createAccount(user.getName());
        users().insert(query);
    }

    @Override
    public User getBy(String username) {

        DBObject query = new BasicDBObject("username", username);

        DBObject projection = new BasicDBObject("username", 1)
                .append("password", 1);

        BasicDBObject dbObject = (BasicDBObject) users().findOne(query, projection);

        if(dbObject != null) {
            User user = new User();

            user.setName(dbObject.getString("username"));
            user.setPassword(dbObject.getString("password"));

            return new User();
        }

        return null;
    }

    private DBCollection users() {
        return db.getCollection("users");
    }

    private void createAccount(String name){

        BasicDBObject query = new BasicDBObject();

        query.append("name", name);
        query.append("amount",0);

        db.getCollection("bank_accounts").insert(query);
    }
}