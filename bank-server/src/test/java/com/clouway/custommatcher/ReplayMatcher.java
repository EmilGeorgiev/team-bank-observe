package com.clouway.custommatcher;

import com.google.sitebricks.headless.Reply;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.lang.reflect.Field;

/**
 * Created by emil on 14-9-27.
 */
public class ReplayMatcher<T> {

    public Matcher matches(final Object expected) {

        return new TypeSafeMatcher<Reply>() {
            @Override
            public boolean matchesSafely(Reply actual) {

                Class clazz = actual.getClass();

                Field field;

                try {
                    field = clazz.getDeclaredField("entity");

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
