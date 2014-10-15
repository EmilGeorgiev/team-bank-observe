package com.clouway.http;

import com.clouway.core.SiteMap;
import com.clouway.core.User;
import com.clouway.core.UserRepository;
import com.clouway.core.UserValidator;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * Created by clouway on 14-10-2.
 */
public class RegistrationCtrlTest {

    private RegistrationCtrl registrationCtrl;
    private User user;

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    @Mock
    UserRepository repository;

    @Mock
    UserValidator validator;

    @Mock
    SiteMap siteMap;

    @Before
    public void setUp() throws Exception {
        user = new User();
        registrationCtrl = new RegistrationCtrl(validator, repository, siteMap);
    }

    @Test
    public void registerNewUser() {

        context.checking(new Expectations() {
            {
                oneOf(validator).isValid(user);
                will(returnValue(true));

                oneOf(repository).getBy(user.getName());
                will(returnValue(null));

                oneOf(repository).add(user);
            }
        });

        assertThat(registrationCtrl.register(), is("/login"));
    }

    @Test
    public void tryRegisterUserWhoAlreadyExist() {

        context.checking(new Expectations() {
            {
                oneOf(validator).isValid(user);
                will(returnValue(true));

                oneOf(repository).getBy(user.getName());
                will(returnValue(new User()));

                oneOf(siteMap).registrationError();
            }
        });

        assertThat(registrationCtrl.register(), nullValue());
    }

    @Test
    public void userDataAreNotCorrect() {

        context.checking(new Expectations() {
            {
                oneOf(validator).isValid(user);
                will(returnValue(false));

                oneOf(siteMap).registrationError();
            }
        });

        assertThat(registrationCtrl.register(), nullValue());
    }
}