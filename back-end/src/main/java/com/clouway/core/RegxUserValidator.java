package com.clouway.core;

/**
 * Created by clouway on 14-10-2.
 */
public class RegxUserValidator implements UserValidator{
    @Override
    public boolean isValid(User user) {
        return user.getName() != null && user.getName().matches("^[A-Za-z]{5,12}?$") && user.getPassword().matches("^[0-9a-zA-Z]{6,18}?$");
    }
}