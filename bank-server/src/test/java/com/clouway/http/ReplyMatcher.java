package com.clouway.http;

import com.google.sitebricks.headless.Reply;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.lang.reflect.Field;

/**
 * Created by clouway on 14-9-26.
 */
public class ReplyMatcher {

    Field field;
    Class aClass;
    String str;

    protected Matcher<Reply> is(final String obj) {

        return new TypeSafeMatcher<Reply>() {
            @Override
            protected boolean matchesSafely(Reply reply) {

                try {
                    aClass = reply.getClass();
                    field = aClass.getDeclaredField("redirectUri");
                    field.setAccessible(true);
                    str = (String) field.get(reply);
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    e.printStackTrace();
                }
                return str.equals(obj);
            }

            @Override
            public void describeTo(Description description) {

            }
        };
    }
}
