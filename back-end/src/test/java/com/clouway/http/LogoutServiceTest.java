package com.clouway.http;

import com.clouway.core.Session;
import com.clouway.core.SessionRepository;
import com.clouway.core.SiteMap;
import com.google.inject.util.Providers;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Date;

import static com.clouway.custommatcher.ReplyMatcher.contains;
import static org.hamcrest.MatcherAssert.assertThat;

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

                oneOf(siteMap).loginPage();
                will(returnValue("/login"));
            }
        });

        assertThat(logoutService.logout(), contains("/login"));
    }
}