package com.clouway.http;

import com.clouway.core.Session;
import com.clouway.core.SessionRepository;
import com.clouway.core.SiteMap;
import com.google.inject.Provider;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by clouway on 14-9-25.
 */
public class LogoutServiceTest {

    LogoutService logoutService;
    Session session;
    ReplyMatcher replyMatcher;


    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    @Mock
    SessionRepository sessionRepository;

    @Mock
    Provider<Session> currentSessionProvider;

    @Mock
    SiteMap siteMap;


    @Before
    public void setUp() {
        logoutService = new LogoutService(sessionRepository, currentSessionProvider, siteMap);

        String username = "Stanislav";
        String sessionId = "sessionId";
        replyMatcher = new ReplyMatcher();

        session = new Session(username, sessionId, new Date());
    }

    @Test
    public void logoutFromSystem() throws NoSuchFieldException, IllegalAccessException {

        context.checking(new Expectations() {
            {
                oneOf(currentSessionProvider).get();
                will(returnValue(session));

                oneOf(siteMap).loginPage();
                will(returnValue("/bank/index.html"));

                oneOf(sessionRepository).remove(session.getSessionId());
            }
        });
        assertThat(logoutService.logout(),replyMatcher.is("/bank/index.html"));
    }
}