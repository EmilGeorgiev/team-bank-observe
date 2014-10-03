package com.clouway.persistent;

import com.clouway.core.User;
import com.clouway.core.UserRepository;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

/**
 * Created by emil on 14-9-27.
 */
public class PersistentUserRepository implements UserRepository {


    private final DB db;

    @Inject
    public PersistentUserRepository(Provider<DB> dbProvider) {

        this.db = dbProvider.get();
    }

    @Override
    public boolean isAuthorised(User user) {

        BasicDBObject query = new BasicDBObject();

        query.append("username", user.getUsername());
        query.append("password", user.getPassword());

        return users().count(query) == 1;
    }

    @Override
    public boolean exists(String username) {

        DBObject query = new BasicDBObject("username", username);

        return users().count(query) == 1;
    }

    @Override
    public void add(User user) {

        BasicDBObject query = new BasicDBObject();

        query.append("username",user.getUsername());
        query.append("password",user.getPassword());

        createAccount(user.getUsername());
        users().insert(query);
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