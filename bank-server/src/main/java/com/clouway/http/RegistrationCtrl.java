package com.clouway.http;

import com.clouway.core.SiteMap;
import com.clouway.core.User;
import com.clouway.core.UserRepository;
import com.clouway.core.UserValidator;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.sitebricks.At;
import com.google.sitebricks.Show;
import com.google.sitebricks.http.Get;
import com.google.sitebricks.http.Post;

/**
 * Created by clouway on 14-10-2.
 */
@At("/registration")
@Show("registration.html")
@Singleton
public class RegistrationCtrl {

    private UserValidator validator;
    private UserRepository repository;
    private User user = new User();
    private SiteMap siteMap;
    private String error;

    @Inject
    public RegistrationCtrl(UserValidator validator, UserRepository repository, SiteMap siteMap) {

        this.validator = validator;
        this.repository = repository;
        this.siteMap = siteMap;
    }

    @Post
    public String register() {

        if (!repository.exists(user.getUsername())){

            if (validator.isCorrect(user)){
                repository.add(user);
                return "/login";
            }
        }
        error = siteMap.registrationError();
        return null;
    }

    @Get
    public void clear(){
        error = null;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public String getError() {
        return error;
    }
}