package com.clouway.core;

/**
 * Created by clouway on 14-9-25.
 */
public interface SiteMap {

    String sessionCookieName();

    String loginPage();

    String index();

    String registrationError();

    String loginFailed();

    String transactionError();
}
