package com.clouway.core;

import java.util.Date;

/**
 * Created by emil on 14-9-27.
 */
public interface SessionRepository {

    CurrentUser getClientName(String sessionId);

    void addUser(String username, String sessionId);

    void remove(String session);

    Session get(String session);

    boolean authenticate(String sessionId);
}