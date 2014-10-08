package com.clouway.http;

import com.clouway.core.Session;
import com.clouway.core.SessionRepository;
import com.google.inject.util.Providers;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class SecurityFilterTest {

    private SecurityFilter securityFilter;
    private Cookie[] cookies;
    private Session session;

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    @Mock
    private SessionRepository sessionRepository = null;

    @Mock
    private HttpServletRequest request = null;

    @Mock
    private FilterChain filterChain;

    @Mock
    private HttpServletResponse response = null;


    @Before
    public void setUp() {

        session = new Session("username","sessionid",new Date());

        securityFilter = new SecurityFilter(sessionRepository, Providers.of(session));

        cookies = new Cookie[]{new Cookie("sid", "abc")};

    }


    @Test
    public void sessionIsNotExpired() throws Exception {

        context.checking(new Expectations() {
            {
                oneOf(sessionRepository).authenticate(sessionID(sid("sessionid")));
                will(returnValue(true));

                oneOf(filterChain).doFilter(request, response);
            }
        });

        securityFilter.doFilter(request, response, filterChain);

    }



    @Test
    public void sessionIsExpired() throws IOException, ServletException {

        context.checking(new Expectations() {{

            oneOf(sessionRepository).authenticate(sessionID(sid("sessionid")));
            will(returnValue(false));

            oneOf(response).setStatus(401);
        }
        });

        securityFilter.doFilter(request, response, filterChain);

    }

    private String sid(String sid) {
        return sid;
    }

    private String sessionID(String session) {
        return session;
    }
}