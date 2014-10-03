package com.clouway.http;

import com.clouway.core.*;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.sitebricks.At;
import com.google.sitebricks.Show;
import com.google.sitebricks.http.Get;
import com.google.sitebricks.http.Post;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by clouway on 14-9-25.
 */

@At("/login")
@Show("/login.html")
@Singleton
public class LoginCtrl {

    private UserRepository userRepository;
    private SessionRepository sessionRepository;
    private IdGenerator idGenerator;
    private SiteMap siteMap;
    private String error;
    private User user = new User();

    @Inject
    public LoginCtrl(UserRepository userRepository, SessionRepository sessionRepository, IdGenerator idGenerator, SiteMap siteMap) {

        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.idGenerator = idGenerator;
        this.siteMap = siteMap;
    }

    @Get
    public void clear(){
        error = null;
    }

    @Post
    public String authorise(HttpServletResponse response) {

        if (!userRepository.isAuthorised(user)) {
            error = siteMap.loginFailed();
            return null;
        }

        String sessionId = idGenerator.generateFor(user);
        sessionRepository.addUser(user.getUsername(), sessionId);
        response.addCookie(new Cookie(siteMap.sessionCookieName(), sessionId));
        return siteMap.index();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getError() {
        return error;
    }
}