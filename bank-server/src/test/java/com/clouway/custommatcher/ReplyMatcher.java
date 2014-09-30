package com.clouway.custommatcher;

import com.google.sitebricks.headless.Reply;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.lang.reflect.Field;

/**
 * Created by emil on 14-9-27.
 */
public class ReplyMatcher {


    /**
     * Equals contain on the <code>Reply</code> with given object.
     * @param expected object who check whether is contain in Reply.
     * @param <T>
     * @return
     */
    public <T> Matcher contains(final T expected, final String fieldName) {

        return new TypeSafeMatcher<Reply>() {
            @Override
            public boolean matchesSafely(Reply actual) {

                Class clazz = actual.getClass();

                Field field;

                try {
                    field = clazz.getDeclaredField(fieldName);

                    field.setAccessible(true);

                    T expected1 = (T) expected;

                    T actual1 = (T) field.get(actual);

                    return actual1.equals(expected1);

                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }

                return false;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(expected.toString());
            }
        };
    }
 }
