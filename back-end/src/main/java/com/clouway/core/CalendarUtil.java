package com.clouway.core;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by clouway on 14-9-26.
 */
public class CalendarUtil implements Clock{

    @Override
    public Date sessionExpirationTime(Calendar calendar) {

        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.DST_OFFSET, 1);

        return calendar.getTime();
    }

    @Override
    public Date now() {
        return new Date();
    }
}
