package com.clouway.core;

import java.util.Date;

/**
 * Created by clouway on 14-9-25.
 */
public class Session {

    private String username;
    private String sessionId;
    private Date date;

    public Session(String username, String sessionId, Date date) {

        this.username = username;
        this.sessionId = sessionId;
        this.date = date;
    }

    public String getSessionId() {
        return sessionId;
    }
}
