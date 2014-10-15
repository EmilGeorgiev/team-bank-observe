package com.clouway.core;

import java.util.Date;

/**
 * Created by clouway on 14-9-25.
 */
public class Session {

    private String username;
    private String sessionId;
    private Date expirationTime;

    public Session(String username, String sessionId, Date expirationTime) {
        this.username = username;
        this.sessionId = sessionId;
        this.expirationTime = expirationTime;
    }

    public Session(String session) {
        this.sessionId = session;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getUsername() {
        return username;
    }

    public Date getExpirationTime() {
        return expirationTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Session session = (Session) o;

        if (sessionId != null ? !sessionId.equals(session.sessionId) : session.sessionId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return sessionId != null ? sessionId.hashCode() : 0;
    }
}
