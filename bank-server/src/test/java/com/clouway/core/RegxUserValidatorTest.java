package com.clouway.core;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by clouway on 14-10-2.
 */
public class RegxUserValidatorTest {

    RegxUserValidator validator;
    User user;

    @Before
    public void setUp() throws Exception {
        user = new User();
        validator = new RegxUserValidator();
    }

    @Test
    public void pretendThatDataAreCorrect() {
        user.setUsername("Brahmaputra");
        user.setPassword("111111");
        assertThat(validator.isCorrect(user), is(true));
    }

    @Test
    public void usernameIsTooShort() {
        user.setUsername("Bra");
        user.setPassword("123456");
        assertThat(validator.isCorrect(user), is(false));
    }

    @Test
    public void usernameIsTooLong() {
        user.setUsername("BrahmaputrafromMountain");
        user.setPassword("123456");
        assertThat(validator.isCorrect(user), is(false));
    }

    @Test
    public void passwordIsTooShort() {
        user.setUsername("Brahmaputra");
        user.setPassword("123");
        assertThat(validator.isCorrect(user), is(false));
    }

    @Test
    public void passwordIsTooLong() {
        user.setUsername("Brahmaputra");
        user.setPassword("123456789123456789123");
        assertThat(validator.isCorrect(user), is(false));
    }
}