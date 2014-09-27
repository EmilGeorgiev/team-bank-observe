package com.clouway.persistent;

import com.clouway.core.SessionRepository;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.mongodb.DB;

/**
 * Created by emil on 14-9-27.
 */
public class PersistentSessionRepository implements SessionRepository {

    private DB db;

    @Inject
    public PersistentSessionRepository(Provider<DB> dbProvider) {

        this.db = dbProvider.get();
    }
}
