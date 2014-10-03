package com.clouway.core;

/**
 * Created by clouway on 14-10-2.
 */
public class RegxUserValidator implements UserValidator{
    @Override
    public boolean isCorrect(User user) {
        return user.getUsername() != null && user.getUsername().matches("^[A-Za-z]{5,12}?$") && user.getPassword().matches("^[0-9a-zA-Z]{6,18}?$");
    }
}