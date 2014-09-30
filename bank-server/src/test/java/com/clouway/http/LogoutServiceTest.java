package com.clouway.http;

import com.clouway.core.Session;
import com.clouway.core.SessionRepository;
import com.clouway.core.SiteMap;
import com.clouway.custommatcher.ReplyMatcher;
import com.google.inject.Provider;
import com.google.inject.util.Providers;
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

    private LogoutService logoutService;
    private Session session;

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    @Mock
    SessionRepository sessionRepository;

    @Mock
    SiteMap siteMap;


    @Before
    public void setUp() {

        String username = "Stanislav";
        String sessionId = "sessionId";
        Date expirationTime = new Date();

        session = new Session(username, sessionId, expirationTime);

        logoutService = new LogoutService(sessionRepository, Providers.of(session), siteMap);
    }

    @Test
    public void logoutFromSystem() throws NoSuchFieldException, IllegalAccessException {

        context.checking(new Expectations() {
            {
                oneOf(sessionRepository).remove(session.getSessionId());

                oneOf(siteMap).index();
                will(returnValue("/bank/index.html"));
            }
        });

        ReplyMatcher<String> replyMatcher = new ReplyMatcher<>();
        assertThat(logoutService.logout(), replyMatcher.matches("/bank/index.html","redirectUri"));
    }
}