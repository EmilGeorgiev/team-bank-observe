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
    public String authenticationError() {
        return "/authenticationError.html";
    }

    @Override
    public String loginPage() {
        return "login.html";
    }

    @Override
    public String index() {
        return "/bin/index.html";
    }
}
