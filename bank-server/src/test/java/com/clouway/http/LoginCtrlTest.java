package com.clouway.http;

import com.clouway.core.*;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.servlet.http.Cookie;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;

/**
 * Created by clouway on 14-9-24.
 */
public class LoginCtrlTest {

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    private User user;
    private LoginCtrl loginCtrl;
    private FakeHttpResponse response;

    @Mock
    UserRepository userRepository;

    @Mock
    SessionRepository sessionRepository;

    @Mock
    IdGenerator idGenerator;

    @Mock
    SiteMap siteMap;

    @Before
    public void setUp(){

        user = new User();
        loginCtrl = new LoginCtrl(userRepository, sessionRepository, idGenerator, siteMap);

        response = new FakeHttpResponse(){
            @Override
            public void addCookie(Cookie cookie) {
                super.addCookie(cookie);
            }
        };
    }


    @Test
    public void itShouldLogin() {

        context.checking(new Expectations(){
            {
                oneOf(userRepository).isAuthorised(user);
                will(returnValue(true));

                oneOf(idGenerator).generateFor(user);
                will(returnValue("sessionId"));

                oneOf(siteMap).index();
                will(returnValue("index.html"));

                oneOf(siteMap).sessionCookieName();
                will(returnValue("sId"));

                oneOf(sessionRepository).addUser(user.getUsername(), "sessionId");
            }
        });
        assertThat(loginCtrl.authorise(response), is("index.html"));
    }

    @Test
    public void loginFailed() {

        context.checking(new Expectations(){
            {
                oneOf(userRepository).isAuthorised(user);
                will(returnValue(false));

                oneOf(siteMap).loginFailed();
                will(returnValue("Error"));
            }
        });
        assertThat(loginCtrl.authorise(response), nullValue());
    }
}