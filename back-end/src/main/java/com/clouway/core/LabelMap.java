package com.clouway.core;

/**
 * Created by clouway on 14-9-26.
 */
public class LabelMap implements SiteMap{
    @Override
    public String sessionCookieName() {
        return "sid";
    }

    @Override
    public String loginPage() {
        return "/login";
    }

    @Override
    public String index() {
        return "/bin/index.html";
    }

    @Override
    public String registrationError() {
        return "Registration Failed";
    }

    @Override
    public String loginFailed() {
        return "Login Failed";
    }

    @Override
    public String transactionError() {
        return "Transaction error";
    }
}