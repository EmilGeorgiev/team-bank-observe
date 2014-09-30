package com.clouway.http;

import com.clouway.core.Session;
import com.clouway.core.SessionRepository;
import com.clouway.core.SiteMap;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.sitebricks.At;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Service;
import com.google.sitebricks.http.Post;

/**
 * Created by clouway on 14-9-25.
 */
@At("/logout")
@Service
@Singleton
public class LogoutService {

    private SessionRepository sessionRepository;
    private Provider<Session> currentSessionProvider;
    private SiteMap siteMap;

    @Inject
    public LogoutService(SessionRepository sessionRepository, Provider<Session> currentSessionProvider, SiteMap siteMap) {
        this.sessionRepository = sessionRepository;
        this.currentSessionProvider = currentSessionProvider;
        this.siteMap = siteMap;
    }

    @Post
    public Reply<?> logout() {

        Session session = currentSessionProvider.get();

        sessionRepository.remove(session.getSessionId());

        return Reply.saying().redirect(siteMap.index());
    }
}