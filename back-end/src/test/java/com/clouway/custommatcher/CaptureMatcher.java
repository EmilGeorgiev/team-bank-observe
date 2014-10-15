package com.clouway.custommatcher;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * Created by emil on 14-9-30.
 */
public class CaptureMatcher<T> extends BaseMatcher<T> {

    private final Matcher<T> baseMatcher;

    private Object captureArgument;

    public CaptureMatcher(Matcher<T> baseMatcher) {
        this.baseMatcher = baseMatcher;
    }


    @Override
    public boolean matches(Object item) {

        captureArgument = item;

        return baseMatcher.matches(captureArgument);

    }

    @Override
    public void describeTo(Description description) {
        baseMatcher.describeTo(description);
    }

    public Object getCaptureArgument() {
        return captureArgument;
    }


}
