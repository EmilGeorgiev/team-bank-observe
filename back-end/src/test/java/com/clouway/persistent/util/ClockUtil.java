package com.clouway.persistent.util;

import com.clouway.core.Clock;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Emil Georgiev <emogeorgiev88@gmail.com>.
 */
public class ClockUtil implements Clock {
    private final int year;
    private final int month;
    private final int day;
    private final int hours;
    private final int minutes;
    private final int seconds;

    public ClockUtil(int year, int month, int day, int hours, int minutes, int seconds) {

        this.year = year;
        this.month = month;
        this.day = day;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    @Override
    public Date sessionExpirationTime(Calendar calendar) {
        return calendar.getTime();
    }

    @Override
    public Date now() {

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, seconds);

        return calendar.getTime();
    }
}
