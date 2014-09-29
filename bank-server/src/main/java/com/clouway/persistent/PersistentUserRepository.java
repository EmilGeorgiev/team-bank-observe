package com.clouway.persistent;

import com.clouway.core.UserRepository;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.mongodb.DB;
import com.mongodb.DBCollection;

/**
 * Created by emil on 14-9-27.
 */
public class PersistentUserRepository implements UserRepository{


    private final DB db;

    @Inject
    public PersistentUserRepository(Provider<DB> dbProvider) {

        this.db = dbProvider.get();
    }

    private DBCollection users() {

        return db.getCollection("users");
    }
}
