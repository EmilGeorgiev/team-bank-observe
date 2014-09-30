package com.clouway.core;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by clouway on 14-9-29.
 */
public interface Clock {

    Date sessionExpirationTime(Calendar calendar);
}
