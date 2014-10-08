package com.clouway.http;

import com.clouway.core.SessionRepository;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.servlet.FilterChain;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SecurityFilterTest {

    private SecurityFilter securityFilter;
    private Cookie[] cookies;

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    @Mock
    private SessionRepository sessionRepository = null;

    @Mock
    private HttpServletRequest request = null;

    @Mock
    private FilterChain filterChain = null;

    @Mock
    private HttpServletResponse response = null;

    @Before
    public void setUp() {

//        securityFilter = new SecurityFilter(sessionRepository, sessionProvider);

        cookies = new Cookie[]{new Cookie("sid", "abc")};

    }


    @Test
    public void sessionIsNotExpired() throws Exception {

        context.checking(new Expectations() {
            {

                oneOf(request).getCookies();
                will(returnValue(cookies));

                oneOf(sessionRepository).authenticate(sessionID(sid("abc")));
                will(returnValue(true));

                oneOf(filterChain).doFilter(request, response);

            }
        });

        securityFilter.doFilter(request, response, filterChain);

    }



    @Test
    public void sessionIsExpired() throws Exception {

//        securityFilter = new SecurityFilter(sessionRepository, sessionProvider);

        context.checking(new Expectations() {{

            oneOf(request).getCookies();
            will(returnValue(cookies));

            oneOf(sessionRepository).authenticate(sessionID(sid("abc")));
            will(returnValue(false));

            oneOf(response).setStatus(401);

            oneOf(filterChain).doFilter(request, response);
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